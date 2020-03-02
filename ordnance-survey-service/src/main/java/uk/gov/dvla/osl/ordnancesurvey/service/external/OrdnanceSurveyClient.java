package uk.gov.dvla.osl.ordnancesurvey.service.external;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dvla.osl.addresslookup.api.os.DpaResults;

@Builder
@Slf4j
public class OrdnanceSurveyClient {

    @NonNull
    private URI ordnanceSurveyBaseUri;

    @NonNull
    private Client client;

    @NonNull
    private ObjectMapper objectMapper;

    @NonNull
    private String apiKey;

    private String searchByPostCodePath() {
        return ordnanceSurveyBaseUri.toString() + "places/v1/addresses/postcode";
    }

    private String matchAddressPath() {
        return ordnanceSurveyBaseUri.toString() + "places/v1/addresses/match";
    }

    private String healthCheck() {
        return ordnanceSurveyBaseUri.toString() + "places/v1/addresses";
    }

    /**
     * get list of addresses for the postcode.
     *
     * @param postCode - postcode to search for
     * @return DpaResults which contains the addresses
     */
    public DpaResults getAddressList(final String postCode) {

        log.info("OrdnanceSurveyClient.getAddressList: calling with {}", postCode);
        DpaResults entity;
        WebTarget webTarget = client.target(searchByPostCodePath())
            .queryParam("postcode", postCode)
            .queryParam("key", apiKey);

        String postcodePath = searchByPostCodePath();
        log.info("OrdnanceSurveyClient.getAddressList: webTarget setup: {}", postcodePath);

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        try (Response response = invocationBuilder.get()) {

            log.info("OrdnanceSurveyClient.getAddressList: response {}", response);

            entity = response.readEntity(DpaResults.class);
        }
        return entity;
    }

    /**
     * Match places based on an input address.
     *
     * @param address - address to match
     * @return DpaResults which contains the places that matches the address
     */
    public DpaResults matchAddress(final String address) {

        log.info("OrdnanceSurveyClient.matchAddress: matching with {}", address);
        DpaResults entity;
        WebTarget webTarget = client.target(matchAddressPath())
            .queryParam("query", address)
            .queryParam("key", apiKey)
            .queryParam("minmatch", "1.0");

        String addressPath = matchAddressPath();
        log.info("OrdnanceSurveyClient.matchAddress: match address URL Path {}", addressPath);

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        try (Response response = invocationBuilder.get()) {

            log.info("OrdnanceSurveyClient.matchAddress: response {}", response);

            entity = response.readEntity(DpaResults.class);
        }
        return entity;
    }

    /**
     * Returns 200 if healthy
     *
     * @return the HTTP status code
     */
    public int health() {
        int status;
        WebTarget webTarget = client.target(healthCheck());
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        try (Response response = invocationBuilder.get()) {
            status = response.getStatus();

//      Due to Ordnance Survey API having no ping/health/status/endpoint we call
//      the API with no API key and expect a 401 - Not Authorised response
//      We cannot make a valid call to the API with our API key as that would
//      quickly consume all of our credits and cost us financially
            if (status == HttpStatus.SC_UNAUTHORIZED) {
                status = HttpStatus.SC_OK;
            }
        }
        log.debug("Health returned HTTP status {}", status);
        return status;
    }
}

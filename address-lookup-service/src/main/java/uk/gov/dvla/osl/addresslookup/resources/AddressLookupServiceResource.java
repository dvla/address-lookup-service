package uk.gov.dvla.osl.addresslookup.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dvla.osl.addresslookup.common.AddressLookupAPI;
import uk.gov.dvla.osl.addresslookup.exceptions.AddressLookupServiceErrors;
import uk.gov.dvla.osl.addresslookup.services.OrdnanceSurveyServiceClient;
import uk.gov.dvla.osl.commons.AddressPAF;
import uk.gov.dvla.osl.commons.error.api.response.ErrorInfo;
import uk.gov.dvla.osl.commons.exception.ApplicationException;
import uk.gov.dvla.osl.dropwizard.filters.annotations.CorrelationIdOptional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/address-lookup/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CorrelationIdOptional
@Slf4j
public final class AddressLookupServiceResource implements AddressLookupAPI {

    private final OrdnanceSurveyServiceClient ordnanceSurveyServiceClient;

    public AddressLookupServiceResource(OrdnanceSurveyServiceClient ordnanceSurveyServiceClient) {
        this.ordnanceSurveyServiceClient = ordnanceSurveyServiceClient;
    }

    @GET
    @Path("postcode/{postcode}")
    public Response getAddressList(@PathParam("postcode") final String postcode) {
        log.info("getAddressList({}) method started...", postcode);
        try {
            log.debug("getAddressList({}) using service client: {}...", postcode, ordnanceSurveyServiceClient);

            List<AddressPAF> results = ordnanceSurveyServiceClient.getAddressList(postcode);

            log.debug("getAddressList({}) search found: '{}' addresses...", postcode, results.size());

            return Response.ok(new ObjectMapper().writeValueAsString(results)).build();

        } catch (ApplicationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ApplicationException(new ErrorInfo(AddressLookupServiceErrors.UNEXPECTED_EXCEPTION_GET_ADDRESSES_BY_POSTCODE_ERROR), e);
        }
    }

    @GET
    @Path("address/{address:.+}")
    public Response matchAddress(@PathParam("address") final String address) {
        try {
            log.debug("matchAddress({}) using service client: {}...", address, ordnanceSurveyServiceClient);

            List<AddressPAF> results = ordnanceSurveyServiceClient.matchAddress(address);

            log.debug("matchAddress({}) search found: '{}' addresses...", address, results.size());

            return Response.ok(new ObjectMapper().writeValueAsString(results)).build();

        } catch (Exception e) {
            log.warn("matchAddress({}) raised an exception {}", address, e);
            return Response.ok("[]").build();
        }
    }

}

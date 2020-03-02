package uk.gov.dvla.osl.addresslookup.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.NonNull;
import uk.gov.dvla.osl.addresslookup.api.os.DPA;
import uk.gov.dvla.osl.addresslookup.api.os.DpaResults;
import uk.gov.dvla.osl.commons.AddressPAF;

@Builder
public class OrdnanceSurveyServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrdnanceSurveyServiceClient.class);

    @NonNull
    private URI ordnanceSurveyBaseUri;

    @NonNull
    private Client client;

    @NonNull
    private ObjectMapper objectMapper;

    private String basePath() {
        return ordnanceSurveyBaseUri.toString();
    }

    /**
     * get list of addresses for the postcode.
     */
    public List<AddressPAF> getAddressList(final String postCode) {
        LOGGER.info("getAddressList({}) method started...", postCode);

        List<AddressPAF> addresses;
        String webTargetURL = basePath() + "postcode/" + postCode;

        WebTarget webTarget = client.target(webTargetURL);

        LOGGER.debug("getAddressList({}) web target setup: {}", postCode, webTargetURL);

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        try (Response response = invocationBuilder.get()) {

            LOGGER.debug("getAddressList({}) client response: {}", postCode, response);

            Optional<DpaResults> result = Optional.ofNullable(response.readEntity(DpaResults.class));

            if (!result.isPresent() || result.get().getRecordArray() == null) {
                LOGGER.debug("getAddressList({}) no results were returned from remote service...", postCode);
                return Collections.emptyList();
            }

            LOGGER.debug("getAddressList({}) client records found: {}", postCode, result.get().getDpaHeader().getTotalresults());
            addresses = getPAFAddresses(result);
        }
        return addresses;

    }

    /**
     * Search places by matching the input address.
     */
    public List<AddressPAF> matchAddress(final String address) {
        LOGGER.info("matchAddress({}) method started...", address);

        List<AddressPAF> addresses;
        String webTargetURL = basePath() + "address/" + address;

        WebTarget webTarget = client.target(webTargetURL);

        LOGGER.debug("matchAddress({}) web target setup: {}", address, webTargetURL);

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        try (Response response = invocationBuilder.get()) {

            LOGGER.debug("matchAddress({}) client response: {}", address, response);

            Optional<DpaResults> result = Optional.ofNullable(response.readEntity(DpaResults.class));

            if (!result.isPresent() || result.get().getRecordArray() == null) {
                LOGGER.debug("matchAddress({}) no results were returned from remote service...", address);
                return Collections.emptyList();
            }

            LOGGER.debug("matchAddress({}) client records found: {}", address, result.get().getDpaHeader().getTotalresults());

            addresses = getPAFAddresses(result);
        }
        return addresses;
    }

    private List<AddressPAF> getPAFAddresses(Optional<DpaResults> result) {
        // turn into the common address format
        List<AddressPAF> addresses = new ArrayList<>();

        if (result.isPresent()) {
            for (DPA dpa : result.get().getRecordArray()) {
                AddressPAF address = AddressPAF.builder()
                    .UPRN(dpa.getUprn())
                    .UDPRN(dpa.getUdprn())
                    .address(dpa.getAddress())
                    .poBoxNumber(dpa.getPoBoxNumber())
                    .organisationName(dpa.getOrganisationName())
                    .departmentName(dpa.getDepartmentName())
                    .subBuildingName(dpa.getSubBuildingName())
                    .buildingName(dpa.getBuildingName())
                    .buildingNumber(dpa.getBuildingNumber())
                    .dependentThoroughfareName(dpa.getDependentThoroughfareName())
                    .thoroughfareName(dpa.getThoroughfareName())
                    .doubleDependentLocality(dpa.getDoubleDependentLocality())
                    .dependentLocality(dpa.getDependentLocality())
                    .postTown(dpa.getPostTown())
                    .postCode(dpa.getPostCode())
                    .RPC(dpa.getRpc())
                    .xCoordinate(dpa.getXCoordinate())
                    .yCoordinate(dpa.getYCoordinate())
                    .status(dpa.getStatus())
                    .match(dpa.getMatch())
                    .matchDescription(dpa.getMatchDescription())
                    .language(dpa.getLanguage())
                    .localCustodianCode(dpa.getLocalCustodianCode())
                    .localCustodianCodeDescription(dpa.getLocalCustodianCodeDescription())
                    .classificationCode(dpa.getClassificationCode())
                    .classificationCodeDescription(dpa.getClassificationCodeDescription())
                    .postalAddressCode(dpa.getPostalAddressCode())
                    .postalAddressCodeDescription(dpa.getPostalAddressCodeDescription())
                    .logicalStatusCode(dpa.getLogicalStatusCode())
                    .BLPUStateCode(dpa.getBlpuStateCode())
                    .BLPUStateCodeDescription(dpa.getBlpuStateCodeDescription())
                    .topographyLayerToId(dpa.getTopographyLayerToId())
                    .parentUPRN(dpa.getParentUPRN())
                    .lastUpdateDate(dpa.getLastUpdateDate())
                    .entryDate(dpa.getEntryDate())
                    .legalName(dpa.getLegalName())
                    .BLPUStateDate(dpa.getBlpuStateDate())
                    .build();

                LOGGER.trace(address.getAddress());

                addresses.add(address);
            }
        }
        return addresses;
    }

}

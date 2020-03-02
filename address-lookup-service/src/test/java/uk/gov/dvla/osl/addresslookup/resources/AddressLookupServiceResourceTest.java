package uk.gov.dvla.osl.addresslookup.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.gov.dvla.osl.addresslookup.api.os.DPA;
import uk.gov.dvla.osl.addresslookup.api.os.DpaResults;
import uk.gov.dvla.osl.addresslookup.exceptions.AddressLookupServiceErrors;
import uk.gov.dvla.osl.addresslookup.services.OrdnanceSurveyServiceClient;
import uk.gov.dvla.osl.commons.AddressPAF;
import uk.gov.dvla.osl.commons.error.api.response.ErrorInfo;
import uk.gov.dvla.osl.commons.exception.ApplicationException;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AddressLookupServiceResourceTest {

    private OrdnanceSurveyServiceClient mockClient = Mockito.mock(OrdnanceSurveyServiceClient.class);
    private AddressLookupServiceResource addressLookupResource;
    private List<AddressPAF> testData;

    @BeforeEach
    void setup() throws Exception {

        addressLookupResource = new AddressLookupServiceResource(mockClient);
        testData = loadTestData();
    }

    @Test
    void testGetAddressSuccess() {
        when(mockClient.getAddressList("SA11JN")).thenReturn(testData);
        Response expectedResponse = addressLookupResource.getAddressList("SA11JN");
        assertThat(expectedResponse, notNullValue());
        String expectedDpaResult = (String) expectedResponse.getEntity();
        assertThat(expectedDpaResult, notNullValue());
        List<AddressPAF> addressPAFList = constructDpaFromJSON(expectedDpaResult);
        assertThat(addressPAFList.size(), greaterThan(0));
    }

    @Test
    void testGetAddressThrowsException() {
        when(mockClient.getAddressList("SA11JN")).thenThrow(new ApplicationException(new ErrorInfo(
                AddressLookupServiceErrors.UNEXPECTED_EXCEPTION_GET_ADDRESSES_BY_POSTCODE_ERROR)));

        assertThrows(ApplicationException.class, () -> addressLookupResource.getAddressList("SA11JN"));
    }

    @Test
    void testMatchAddressSuccess() {
        when(mockClient.matchAddress("Meridian Tower")).thenReturn(testData);
        Response expectedResponse = addressLookupResource.matchAddress("Meridian Tower");
        assertThat(expectedResponse, notNullValue());
        String expectedDpaResult = (String) expectedResponse.getEntity();
        assertThat(expectedDpaResult, notNullValue());
        assertThat(expectedDpaResult.length(), greaterThan(0));
    }

    @Test
    void testMatchAddressCatchesException()  {

        when(mockClient.matchAddress("Meridian Tower")).thenThrow(new ApplicationException(new ErrorInfo(
                AddressLookupServiceErrors.UNEXPECTED_EXCEPTION_MATCH_ADDRESSES_ERROR)));

        Response expectedResponse = addressLookupResource.matchAddress("Meridian Tower");
        assertThat(expectedResponse, notNullValue());
        String expectedDpaResult = (String) expectedResponse.getEntity();
        assertThat(expectedDpaResult, notNullValue());
        List<AddressPAF> addressPAFList = constructDpaFromJSON(expectedDpaResult);
        assertThat(addressPAFList.size(), equalTo(0));
    }

    private List<AddressPAF> loadTestData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream("from_stub.json");

        DpaResults dpaResults = mapper.readValue(resourceStream, DpaResults.class);

        List<AddressPAF> addresses = new ArrayList<>();

        for (DPA dpa : dpaResults.getRecordArray()) {
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

            addresses.add(address);
        }
        return addresses;
    }


    private List<AddressPAF> constructDpaFromJSON(String addressListString) {
        List<AddressPAF> results;
        try {
            results = Json.mapper().readValue(addressListString, new TypeReference<List<AddressPAF>>() {
            });
        } catch (Exception ex) {
            return Collections.emptyList();
        }
        return results;
    }

}

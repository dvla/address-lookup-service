package uk.gov.dvla.osl.ordnancesurvey.service.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.gov.dvla.osl.addresslookup.api.os.DpaResults;
import uk.gov.dvla.osl.commons.error.api.response.ErrorInfo;
import uk.gov.dvla.osl.commons.exception.ApplicationException;
import uk.gov.dvla.osl.ordnancesurvey.service.errors.OrdnanceSurveyServiceErrors;
import uk.gov.dvla.osl.ordnancesurvey.service.external.OrdnanceSurveyClient;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class OrdnanceSurveyServiceResourceTest {

	private OrdnanceSurveyClient mockClient = Mockito.mock(OrdnanceSurveyClient.class);
	private OrdnanceSurveyServiceResource serviceResource;
	private DpaResults testData;

	@BeforeEach
    void setup() throws Exception{
		serviceResource = new OrdnanceSurveyServiceResource(mockClient);
		testData = loadTestData();
	}

	@Test
	void testGetAddressSuccess(){
		when(mockClient.getAddressList(anyString())).thenReturn(testData);
		Response expectedResponse = serviceResource.getAddressList(anyString());
		assertThat(expectedResponse, notNullValue());
		DpaResults expectedDpaResult = (DpaResults)expectedResponse.getEntity();
		assertThat(expectedDpaResult, notNullValue());
		assertThat(expectedDpaResult.getRecordArray().size(), equalTo(100));
	}

    @Test
    void testGetAddressThrowsApplicationException() {

        when(mockClient.getAddressList(anyString())).thenThrow(new ApplicationException(new ErrorInfo(
                OrdnanceSurveyServiceErrors.UNEXPECTED_EXCEPTION_GET_ADDRESSES_BY_POSTCODE_ERROR)));

        assertThrows(ApplicationException.class, () -> serviceResource.getAddressList(anyString()));
    }

	@Test
	void testMatchAddressSuccess(){
		when(mockClient.matchAddress(anyString())).thenReturn(testData);
		Response expectedResponse = serviceResource.matchAddress(anyString());
		assertThat(expectedResponse, notNullValue());
		DpaResults expectedDpaResult = (DpaResults)expectedResponse.getEntity();
		assertThat(expectedDpaResult, notNullValue());
		assertThat(expectedDpaResult.getRecordArray().size(), equalTo(100));
	}

	@Test
	void testMatchAddressThrowsApplicationException(){

		when(mockClient.matchAddress(anyString())).thenThrow(new ApplicationException(new ErrorInfo(
                    OrdnanceSurveyServiceErrors.UNEXPECTED_EXCEPTION_MATCH_ADDRESSES_ERROR)));

        assertThrows(ApplicationException.class, () -> serviceResource.matchAddress(anyString()));
	}

	private DpaResults loadTestData() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream("test_data.json");

		return mapper.readValue(resourceStream, DpaResults.class);
	}
}

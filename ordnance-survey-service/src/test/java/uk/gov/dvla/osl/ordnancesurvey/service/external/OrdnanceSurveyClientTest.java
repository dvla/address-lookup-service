package uk.gov.dvla.osl.ordnancesurvey.service.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.gov.dvla.osl.addresslookup.api.os.DpaResults;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

class OrdnanceSurveyClientTest {

    private URI ordnanceSurveyBaseUri = URI.create("http://localhost");
    private Client mockClient = Mockito.mock(Client.class, RETURNS_DEEP_STUBS);
    private ObjectMapper mockObjectMapper = Mockito.mock(ObjectMapper.class);
    private WebTarget mockWebTarget1 = Mockito.mock(WebTarget.class);
    private WebTarget mockWebTarget2 = Mockito.mock(WebTarget.class);
    private WebTarget mockWebTarget3 = Mockito.mock(WebTarget.class);
    private WebTarget mockWebTarget4 = Mockito.mock(WebTarget.class);

    private Invocation.Builder mockInvocationBuilder = Mockito.mock(Invocation.Builder.class);
    private Response mockResponse = Mockito.mock(Response.class);

    private OrdnanceSurveyClient ordnanceClient;

    private DpaResults testData;


    @BeforeEach
    void setup() throws Exception {
        testData = loadTestData();

        ordnanceClient = OrdnanceSurveyClient.builder()
                .ordnanceSurveyBaseUri(ordnanceSurveyBaseUri)
                .client(mockClient)
                .objectMapper(mockObjectMapper)
                .apiKey("abc123").build();
    }

    @Test
    void testGetAddressListSuccess() {
        when(mockClient.target(anyString())).thenReturn(mockWebTarget1);
        when(mockWebTarget1.queryParam(anyString(), anyString())).thenReturn(mockWebTarget2);
        when(mockWebTarget2.queryParam(anyString(), anyString())).thenReturn(mockWebTarget3);
        when(mockWebTarget3.request(anyString())).thenReturn(mockInvocationBuilder);
        when(mockInvocationBuilder.get()).thenReturn(mockResponse);
        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.ACCEPTED);
        when(mockResponse.readEntity(DpaResults.class)).thenReturn(testData);
        DpaResults expectedResults = ordnanceClient.getAddressList(anyString());
        assertThat(expectedResults, notNullValue());
        assertThat(expectedResults.getRecordArray().size(), equalTo(100));
    }

    @Test
    void testMatchAddressListSuccess() {
        when(mockClient.target(anyString())).thenReturn(mockWebTarget1);
        when(mockWebTarget1.queryParam(anyString(), anyString())).thenReturn(mockWebTarget2);
        when(mockWebTarget2.queryParam(anyString(), anyString())).thenReturn(mockWebTarget3);
        when(mockWebTarget3.queryParam(anyString(), anyString())).thenReturn(mockWebTarget4);
        when(mockWebTarget4.request(anyString())).thenReturn(mockInvocationBuilder);
        when(mockInvocationBuilder.get()).thenReturn(mockResponse);
        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.ACCEPTED);
        when(mockResponse.readEntity(DpaResults.class)).thenReturn(testData);
        DpaResults expectedResults = ordnanceClient.matchAddress(anyString());
        assertThat(expectedResults, notNullValue());
        assertThat(expectedResults.getRecordArray().size(), equalTo(100));
    }

    private DpaResults loadTestData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream("test_data.json");

        return mapper.readValue(resourceStream, DpaResults.class);
    }
}

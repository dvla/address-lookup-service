package uk.gov.dvla.osl.addresslookup.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.gov.dvla.osl.addresslookup.api.os.DpaResults;
import uk.gov.dvla.osl.commons.AddressPAF;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

class OrdnanceSurveyServiceClientTest {
    private URI ordnanceSurveyBaseUri = URI.create("http://localhost");
    private Client mockClient = Mockito.mock(Client.class, RETURNS_DEEP_STUBS);
    private ObjectMapper mockObjectMapper = Mockito.mock(ObjectMapper.class);
    private WebTarget mockWebTarget = Mockito.mock(WebTarget.class);

    private Invocation.Builder mockInvocationBuilder = Mockito.mock(Invocation.Builder.class);
    private Response mockResponse = Mockito.mock(Response.class);

    private OrdnanceSurveyServiceClient ordnanceServiceClient;

    private DpaResults testData;

    @BeforeEach
    void setup() throws Exception {
        testData = loadTestData();

        ordnanceServiceClient = OrdnanceSurveyServiceClient.builder()
                .ordnanceSurveyBaseUri(ordnanceSurveyBaseUri)
                .client(mockClient)
                .objectMapper(mockObjectMapper)
                .build();
    }

    @Test
    void testGetAddressListSuccess() {
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(anyString())).thenReturn(mockInvocationBuilder);
        when(mockInvocationBuilder.get()).thenReturn(mockResponse);
        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.ACCEPTED);
        when(mockResponse.readEntity(DpaResults.class)).thenReturn(testData);
        List<AddressPAF> expectedResults = ordnanceServiceClient.getAddressList(anyString());
        assertThat(expectedResults, notNullValue());
        assertThat(expectedResults.size(), equalTo(100));
    }

    @Test
    void testGetAddressListReturnEmpty() {
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(anyString())).thenReturn(mockInvocationBuilder);
        when(mockInvocationBuilder.get()).thenReturn(mockResponse);
        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.ACCEPTED);
        when(mockResponse.readEntity(DpaResults.class)).thenReturn(DpaResults.builder().build());
        List<AddressPAF> expectedResults = ordnanceServiceClient.getAddressList(anyString());
        assertThat(expectedResults, notNullValue());
        assertThat(expectedResults.size(), equalTo(0));
    }

    @Test
    void testMatchAddressSuccess() {
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(anyString())).thenReturn(mockInvocationBuilder);
        when(mockInvocationBuilder.get()).thenReturn(mockResponse);
        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.ACCEPTED);
        when(mockResponse.readEntity(DpaResults.class)).thenReturn(testData);
        List<AddressPAF> expectedResults = ordnanceServiceClient.matchAddress(anyString());
        assertThat(expectedResults, notNullValue());
        assertThat(expectedResults.size(), equalTo(100));
    }

    @Test
    void testMatchAddressReturnEmpty() {
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request(anyString())).thenReturn(mockInvocationBuilder);
        when(mockInvocationBuilder.get()).thenReturn(mockResponse);
        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.ACCEPTED);
        when(mockResponse.readEntity(DpaResults.class)).thenReturn(DpaResults.builder().build());
        List<AddressPAF> expectedResults = ordnanceServiceClient.matchAddress(anyString());
        assertThat(expectedResults, notNullValue());
        assertThat(expectedResults.size(), equalTo(0));
    }

    private DpaResults loadTestData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream("from_stub.json");

        return mapper.readValue(resourceStream, DpaResults.class);
    }
}

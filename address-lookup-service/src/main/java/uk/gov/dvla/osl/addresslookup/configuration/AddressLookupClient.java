package uk.gov.dvla.osl.addresslookup.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public final class AddressLookupClient
{
    @Valid
    @NotNull
    @JsonProperty("service")
    private String service;

    @JsonProperty("baseUrl")
    private String baseUrl;

    /**
     * Standard constructor.
     */
    @JsonCreator
    public AddressLookupClient(@JsonProperty("service") final String service,
                               @JsonProperty("baseUrl") final String baseUrl) {
        this.service = service;
        this.baseUrl = baseUrl;
    }

    /**
     * Return the service on which we will execute the request.
     * @return the implementation for the external service
     */
    public String getService() {
        return service;
    }

    /**
     * Return the baseUrl on which we will execute the request.
     * @return the baseUrl for the external service
     */
    public String getBaseUrl() {
        return baseUrl;
    }
}
package uk.gov.dvla.osl.addresslookup.api.os;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access= AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("header")
public class DpaHeader
{
    @JsonProperty("uri")
    private String uri;

    @JsonProperty("query")
    private String query;

    @JsonProperty("offset")
    private String offset;

    @JsonProperty("totalresults")
    private String totalresults;

    @JsonProperty("format")
    private String format;

    @JsonProperty("dataset")
    private String dataset;

    @JsonProperty("lr")
    private String lr;

    @JsonProperty("maxresults")
    private String maxresults;

    @JsonProperty("epoch")
    private String epoch;

    @JsonProperty("output_srs")
    private String output_srs;
}

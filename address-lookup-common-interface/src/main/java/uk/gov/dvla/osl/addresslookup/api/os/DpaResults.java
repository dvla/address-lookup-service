package uk.gov.dvla.osl.addresslookup.api.os;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor(access= AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DpaResults
{
    @JsonProperty("header")
    private DpaHeader dpaHeader;

    @JsonProperty("results")
    private List<DPA> recordArray;
}

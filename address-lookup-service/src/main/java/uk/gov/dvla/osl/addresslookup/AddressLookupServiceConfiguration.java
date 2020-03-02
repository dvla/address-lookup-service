package uk.gov.dvla.osl.addresslookup;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import lombok.Setter;
import uk.gov.dvla.osl.dropwizard.config.ServiceConfiguration;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddressLookupServiceConfiguration extends Configuration {

    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @NotNull
    @JsonProperty("ordnanceSurveyService")
    private ServiceConfiguration ordnanceSurveyService;

}

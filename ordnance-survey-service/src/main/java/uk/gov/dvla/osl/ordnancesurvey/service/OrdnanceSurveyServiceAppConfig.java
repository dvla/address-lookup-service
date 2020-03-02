package uk.gov.dvla.osl.ordnancesurvey.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import lombok.Setter;
import uk.gov.dvla.osl.dropwizard.config.ThirdPartyServiceConfiguration;

import javax.validation.constraints.NotNull;

@Getter
@Setter
class OrdnanceSurveyServiceAppConfig extends Configuration {

    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @NotNull
    @JsonProperty("ordnanceSurveyThirdPartyService")
    private ThirdPartyServiceConfiguration ordnanceSurveyThirdPartyService;

}
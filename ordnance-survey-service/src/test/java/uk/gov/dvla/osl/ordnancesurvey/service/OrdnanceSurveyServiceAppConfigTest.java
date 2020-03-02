package uk.gov.dvla.osl.ordnancesurvey.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrdnanceSurveyServiceAppConfigTest {

    private YamlConfigurationFactory<OrdnanceSurveyServiceAppConfig> factory;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        Validator validator = Validators.newValidator();
        factory = new YamlConfigurationFactory<>(OrdnanceSurveyServiceAppConfig.class, validator, objectMapper, "dw");
    }

    @Test
    void testAppConfigurationIsValid() throws Exception {
        final OrdnanceSurveyServiceAppConfig config = factory.build(
                new SubstitutingSourceProvider(
                        new ResourceConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                ), "config.yml");
        assertNotNull(config);
    }
}
package uk.gov.dvla.osl.addresslookup;

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

class AddressLookupServiceConfigurationTest {

    private YamlConfigurationFactory<AddressLookupServiceConfiguration> factory;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        Validator validator = Validators.newValidator();
        factory = new YamlConfigurationFactory<>(AddressLookupServiceConfiguration.class, validator, objectMapper, "dw");
    }

    @Test
    void testAppConfigurationIsValid() throws Exception {
        final AddressLookupServiceConfiguration config = factory.build(
                new SubstitutingSourceProvider(
                        new ResourceConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                ), "config.yaml");
        assertNotNull(config);
    }
}
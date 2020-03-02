package uk.gov.dvla.osl.ordnancesurvey.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dvla.osl.dropwizard.bundles.CommonComponentBundle;
import uk.gov.dvla.osl.dropwizard.bundles.JerseyClientBundle;
import uk.gov.dvla.osl.dropwizard.bundles.error.VedErrorBundle;
import uk.gov.dvla.osl.ordnancesurvey.service.errors.ServiceViolationToErrorInfoMapper;
import uk.gov.dvla.osl.ordnancesurvey.service.external.OrdnanceSurveyClient;
import uk.gov.dvla.osl.ordnancesurvey.service.resources.OrdnanceSurveyServiceResource;

import javax.ws.rs.client.Client;

@Slf4j
public class OrdnanceSurveyServiceApplication extends Application<OrdnanceSurveyServiceAppConfig> {

    private static final String READINESS_HEALTH_CHECK_NAME = "api.ordnancesurvey";
    private JerseyClientBundle<OrdnanceSurveyServiceAppConfig> jerseyClientBundle;
    private Client client;
    private OrdnanceSurveyClient ordnanceSurveyClient;

    /**
     * Main method.
     *
     * @param args the args to pass in
     * @throws Exception the exception thrown
     */
    public static void main(String[] args) throws Exception {
        new OrdnanceSurveyServiceApplication().run(args);
    }

    /**
     * Bootstrap the REST service.
     *
     * @param bootstrap configuration file of the application
     */
    @Override
    public void initialize(final Bootstrap<OrdnanceSurveyServiceAppConfig> bootstrap) {

        jerseyClientBundle = JerseyClientBundle.<OrdnanceSurveyServiceAppConfig>builder()
                .jerseyConfigSupplier(ordnanceSurveyServiceAppConfig -> ordnanceSurveyServiceAppConfig.getOrdnanceSurveyThirdPartyService().getJerseyClient())
                .build();

        bootstrap.addBundle(jerseyClientBundle);

        bootstrap.addBundle(
                CommonComponentBundle.<OrdnanceSurveyServiceAppConfig>builder()
                        .artifactId("ordnance-survey-service")
                        .swaggerConfigSupplier( OrdnanceSurveyServiceAppConfig::getSwaggerBundleConfiguration )
                        .readinessHealthCheckNames(new String[]{READINESS_HEALTH_CHECK_NAME})
                        .build()
        );

        withErrorBundle(bootstrap);
    }

    /**
     * Run the application.
     *
     * @param config      application configuration
     * @param environment Dropwizard environment
     * @throws Exception unable to start Dropwizard application
     */
    @Override
    public void run(final OrdnanceSurveyServiceAppConfig config, final Environment environment) throws Exception {

        if ( null == client ) {
            // Note we are sharing this client across components
            client = jerseyClientBundle.createClient("ordnance-survey-service-client");
        }

        if (ordnanceSurveyClient == null) {
            ordnanceSurveyClient = OrdnanceSurveyClient.builder()
                    .ordnanceSurveyBaseUri(config.getOrdnanceSurveyThirdPartyService().getUri())
                    .client(client)
                    .objectMapper( environment.getObjectMapper())
                    .apiKey(config.getOrdnanceSurveyThirdPartyService().getApiKey())
                    .build();
        }

        environment.jersey().register(new OrdnanceSurveyServiceResource(ordnanceSurveyClient));
        environment.healthChecks().register(READINESS_HEALTH_CHECK_NAME, new OrdnanceSurveyServiceHealthCheck(ordnanceSurveyClient));

        log.info("Ordnance Survey Service started successfully.");
    }

    private void withErrorBundle(Bootstrap<OrdnanceSurveyServiceAppConfig> bootstrap) {
        bootstrap.addBundle(new VedErrorBundle(new ServiceViolationToErrorInfoMapper()));
    }
}

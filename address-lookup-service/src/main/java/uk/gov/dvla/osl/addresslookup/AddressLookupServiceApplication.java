package uk.gov.dvla.osl.addresslookup;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dvla.osl.addresslookup.exceptions.ServiceViolationToErrorInfoMapper;
import uk.gov.dvla.osl.addresslookup.resources.AddressLookupServiceResource;
import uk.gov.dvla.osl.addresslookup.services.OrdnanceSurveyServiceClient;
import uk.gov.dvla.osl.dropwizard.bundles.CommonComponentBundle;
import uk.gov.dvla.osl.dropwizard.bundles.JerseyClientBundle;
import uk.gov.dvla.osl.dropwizard.bundles.error.VedErrorBundle;

import javax.ws.rs.client.Client;

@Slf4j
public class AddressLookupServiceApplication extends Application<AddressLookupServiceConfiguration>
{
    private static final String[] READINESS_HEALTH_CHECK_NAMES = new String[] {"ordnanceSurveyService"};

    private JerseyClientBundle<AddressLookupServiceConfiguration> jerseyClientBundle;
    private Client client;
    private OrdnanceSurveyServiceClient ordnanceSurveyClient;

    public static void main(String[] args) throws Exception {
        log.info("main({}) -> Starting Main Service...", (Object) args);

        new AddressLookupServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "Address Lookup Service";
    }

    @Override
    public void initialize(final Bootstrap<AddressLookupServiceConfiguration> bootstrap) {
        log.info("initialize() -> Initialising Service: Address Lookup Service...");

        jerseyClientBundle = JerseyClientBundle.<AddressLookupServiceConfiguration>builder()
                .jerseyConfigSupplier(ordnanceSurveyServiceAppConfig -> ordnanceSurveyServiceAppConfig.getOrdnanceSurveyService().getJerseyClient())
                .build();

        bootstrap.addBundle(jerseyClientBundle);

        bootstrap.addBundle(
                CommonComponentBundle.<AddressLookupServiceConfiguration>builder()
                        .artifactId("address-lookup-service")
                        .swaggerConfigSupplier( AddressLookupServiceConfiguration::getSwaggerBundleConfiguration )
                        .readinessHealthCheckNames(READINESS_HEALTH_CHECK_NAMES)
                        .build()
        );

        withErrorBundle(bootstrap);
    }

    @Override
    public void run(final AddressLookupServiceConfiguration config, final Environment environment) {
        log.info("run() -> Starting Service: [{}]", getName());

        if ( null == client ) {
            client = jerseyClientBundle.createClient("ordnance-survey-service-client");
        }

        if (ordnanceSurveyClient == null) {
            ordnanceSurveyClient = OrdnanceSurveyServiceClient.builder()
                    .ordnanceSurveyBaseUri(config.getOrdnanceSurveyService().getUri())
                    .client(client)
                    .objectMapper(environment.getObjectMapper())
                    .build();
        }

        environment.jersey().register(new AddressLookupServiceResource(ordnanceSurveyClient));

        log.info("[{}] started successfully.", getName());
    }

    private void withErrorBundle(Bootstrap<AddressLookupServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new VedErrorBundle(new ServiceViolationToErrorInfoMapper()));
    }

}

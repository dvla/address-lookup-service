package uk.gov.dvla.osl.ordnancesurvey.stub;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.gov.dvla.osl.dropwizard.bundles.CommonComponentBundle;

public class OrdnanceSurveyServiceStubApplication extends Application<OrdnanceSurveyServiceStubConfiguration>
{
    private static final String READINESS_HEALTH_CHECK_NAME = "getAddressByPostcode";

    public static void main( String[] args ) throws Exception {
        new OrdnanceSurveyServiceStubApplication().run( args );
    }

    @Override
    public String getName() {
        return "Ordnance Survey Service (Stub)";
    }

    @Override
    public void initialize(final Bootstrap<OrdnanceSurveyServiceStubConfiguration> bootstrap) {
        bootstrap.addBundle(
                CommonComponentBundle.<OrdnanceSurveyServiceStubConfiguration>builder()
                        .artifactId("ordnance-survey-service")
                        .swaggerConfigSupplier(OrdnanceSurveyServiceStubConfiguration::getSwaggerBundleConfiguration)
                        .readinessHealthCheckNames(new String[] {READINESS_HEALTH_CHECK_NAME})
                        .build()
        );
    }

    @Override
    public void run( OrdnanceSurveyServiceStubConfiguration configuration, Environment environment ) {
        environment.healthChecks().register(READINESS_HEALTH_CHECK_NAME, new OrdnanceSurveyServiceStubHealthCheck());
        environment.jersey().register(new OrdnanceSurveyServiceStubResource());
    }

}

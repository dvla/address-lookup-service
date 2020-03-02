package uk.gov.dvla.osl.ordnancesurvey.stub;

import com.codahale.metrics.health.HealthCheck;
import uk.gov.dvla.osl.ordnancesurvey.stub.utils.ResourceUtils;

import java.util.Optional;

public class OrdnanceSurveyServiceStubHealthCheck extends HealthCheck {

    private static final String VALID_POSTCODE = "G412EX";
    private static final String ERROR_MSG = "Address could not be retrieved for postcode " + VALID_POSTCODE;

    @Override
    protected HealthCheck.Result check() {
        Optional<String> result = ResourceUtils.loadResponseAsString(VALID_POSTCODE);
        if (result.isPresent()) {
            return Result.healthy("Ready");
        } else {
            return Result.unhealthy(ERROR_MSG);
        }
    }
}

package uk.gov.dvla.osl.ordnancesurvey.service;

import com.codahale.metrics.health.HealthCheck;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import uk.gov.dvla.osl.ordnancesurvey.service.external.OrdnanceSurveyClient;

@Slf4j
@AllArgsConstructor
public class OrdnanceSurveyServiceHealthCheck extends HealthCheck {

    private static final String ERROR_MSG = "Third Party Ordnance Survey System Unavailable, HTTP status - {}";

    private OrdnanceSurveyClient ordnanceSurveyClient;

    @Override
    protected Result check() {
        int status = ordnanceSurveyClient.health();
        if (status == HttpStatus.SC_OK) {
        return Result.healthy("Ready");
        }
        return Result.unhealthy(ERROR_MSG, status);
    }
}

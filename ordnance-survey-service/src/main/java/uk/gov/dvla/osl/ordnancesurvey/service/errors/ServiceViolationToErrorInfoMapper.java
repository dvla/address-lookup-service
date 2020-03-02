package uk.gov.dvla.osl.ordnancesurvey.service.errors;


import uk.gov.dvla.osl.commons.error.api.ErrorCodes;
import uk.gov.dvla.osl.commons.error.api.response.ErrorInfo;
import uk.gov.dvla.osl.commons.exception.mapper.ViolationToErrorInfoMapper;

import javax.validation.ConstraintViolation;
import java.util.stream.Stream;

/**
 * Dealer service error mapper.
 */
public class ServiceViolationToErrorInfoMapper implements ViolationToErrorInfoMapper {

    /**
     * Maps a {@link ConstraintViolation} to an {@link ErrorInfo}
     * @param constraintViolation the constraint violation
     * @return error info
     */
    public ErrorInfo violationToErrorInfo(ConstraintViolation constraintViolation) {

        if (constraintViolation.getInvalidValue() == null) {
            return new ErrorInfo(null, ErrorCodes.CONSTRAINT_VIOLATION_MANDATORY_REQUEST_VALUE.getCode(),
                    ErrorCodes.CONSTRAINT_VIOLATION_MANDATORY_REQUEST_VALUE.getMessage()
                            + " '"
                            + getPropertyName(constraintViolation.getPropertyPath().toString())
                            + "' "
                            + constraintViolation.getMessage(),
                    null);
        } else {
            return new ErrorInfo(null, ErrorCodes.CONSTRAINT_VIOLATION.getCode(),
                    ErrorCodes.CONSTRAINT_VIOLATION.getMessage()
                            + " '"
                            + getPropertyName(constraintViolation.getPropertyPath().toString())
                            + "' "
                            + constraintViolation.getMessage(),
                    null);
        }

    }

    private String getPropertyName(String propertyPath) {
        Stream<String> propertyPathStream = Stream.of(propertyPath.split("\\."));
        return propertyPathStream.reduce((partialResult, element) -> element).orElse("");
    }

}

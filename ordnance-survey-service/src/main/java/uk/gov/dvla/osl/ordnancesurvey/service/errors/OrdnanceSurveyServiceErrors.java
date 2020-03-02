package uk.gov.dvla.osl.ordnancesurvey.service.errors;


import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.dvla.osl.commons.error.api.ErrorCondition;


@AllArgsConstructor
public enum OrdnanceSurveyServiceErrors implements ErrorCondition {

    UNEXPECTED_EXCEPTION_GET_ADDRESSES_BY_POSTCODE_ERROR(9999, "Addresses could not be found by postcode due to an unexpected exception"),
    UNEXPECTED_EXCEPTION_MATCH_ADDRESSES_ERROR(9999, "Addresses could not be matched due to an unexpected exception");

    /**
     * The code for the error condition.
     */
    @Getter
    private Integer code;

    /**
     * The error message associated with the error condition.
     */
    @Getter
    private String message;

}

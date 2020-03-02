package uk.gov.dvla.osl.ordnancesurvey.service.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dvla.osl.addresslookup.api.os.OrdnanceSurveyServiceAPI;
import uk.gov.dvla.osl.commons.error.api.response.ErrorInfo;
import uk.gov.dvla.osl.commons.exception.ApplicationException;
import uk.gov.dvla.osl.dropwizard.filters.annotations.CorrelationIdOptional;
import uk.gov.dvla.osl.ordnancesurvey.service.errors.OrdnanceSurveyServiceErrors;
import uk.gov.dvla.osl.ordnancesurvey.service.external.OrdnanceSurveyClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/ordnancesurvey/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CorrelationIdOptional
@Slf4j
public class OrdnanceSurveyServiceResource implements OrdnanceSurveyServiceAPI {

    private final OrdnanceSurveyClient client;

    public OrdnanceSurveyServiceResource(final OrdnanceSurveyClient client) {
        this.client = client;
    }

    @GET
    @Path("postcode/{postcode}")
    @Timed
    @ExceptionMetered
    public Response getAddressList(@PathParam("postcode") final String postcode) {
        log.info("getAddressList({}) REST API called with postcode...", postcode);

        Response response;
        try {
            response = Response.ok(client.getAddressList(postcode)).build();
        } catch (ApplicationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ApplicationException(Response.Status.BAD_REQUEST.getStatusCode(),
                    new ErrorInfo(OrdnanceSurveyServiceErrors.UNEXPECTED_EXCEPTION_GET_ADDRESSES_BY_POSTCODE_ERROR), e);
        }

        log.info("getAddressList by postcode for address Response: status: {}.", response.getStatus());
        return response;
    }

    @GET
    @Path("address/{address:.+}")
    @Timed
    @ExceptionMetered
    public Response matchAddress(@PathParam("address") final String address) {
        log.info("matchAddress({}) REST API called ...", address);

        Response response;
        try {
            response = Response.ok(client.matchAddress(address)).build();
        } catch (ApplicationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ApplicationException(Response.Status.BAD_REQUEST.getStatusCode(),
                    new ErrorInfo(OrdnanceSurveyServiceErrors.UNEXPECTED_EXCEPTION_MATCH_ADDRESSES_ERROR), e);
        }

        log.info("Match address Response: status: {}.", response.getStatus());
        return response;
    }

}


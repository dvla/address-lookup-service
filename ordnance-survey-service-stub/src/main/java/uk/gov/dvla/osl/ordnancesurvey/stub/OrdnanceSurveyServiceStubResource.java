package uk.gov.dvla.osl.ordnancesurvey.stub;

import lombok.extern.slf4j.Slf4j;
import uk.gov.dvla.osl.addresslookup.api.os.OrdnanceSurveyServiceAPI;
import uk.gov.dvla.osl.ordnancesurvey.stub.utils.ResourceUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/v1/ordnancesurvey/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public final class OrdnanceSurveyServiceStubResource implements OrdnanceSurveyServiceAPI
{
    @GET
    @Path("postcode/{postcode}")
    public Response getAddressList(@PathParam("postcode") final String postcode) {
        log.info("getAddressList({}) REST API called with postcode...", postcode);

        try {
            // Basically postcode validation to simulate a bad request.
            if(postcode == null || postcode.isEmpty()) {
                return Response.noContent().status(Response.Status.BAD_REQUEST).build();
            }

            // Load and return the response in the designated format.
            log.info("getAddressList({}) Searching for content for postcode search...", postcode);
            Optional<String> content = ResourceUtils.loadResponseAsString(postcode.toUpperCase());

            // Postcode data exists, so build and return the response.
            log.info("getAddressList({}) Postcode found in data store: [{}]", postcode, content.isPresent());
            if(content.isPresent()) {
                return Response.ok(content.get()).build();
            }

            // No content was found, so prepare an empty response.
            return Response.ok().build();

        } catch(Exception ex){
            log.error("getAddressList({}) Exception raised searching for postcode!", postcode, ex);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Path("address/{address:.+}")
    public Response matchAddress(@PathParam("address") final String address) {
        log.info("matchAddress({}) REST API called with address...", address);

        try {
            // Basically address validation to simulate a bad request.
            if(address == null || address.isEmpty()) {
                return Response.noContent().status(Response.Status.BAD_REQUEST).build();
            }

            // Load and return the response in the designated format.
            log.info("matchAddress({}) Searching for content for address search...", address);
            Optional<String> content = ResourceUtils.loadResponseAsString(address.toUpperCase());
           
            if(content.isPresent()) {
            	 // Address match exists, so build and return the response.
                log.info("matchAddress({}) Address match found in data store: [{}]", address, Boolean.TRUE);
                
                return Response.ok(content.get()).build();
            }

            // No content was found, so prepare an empty response.
            return Response.ok().build();

        } catch(Exception ex){
            log.error("matchAddress({}) Exception raised searching for address!", address, ex);
            return Response.serverError().build();
        }
    }
}

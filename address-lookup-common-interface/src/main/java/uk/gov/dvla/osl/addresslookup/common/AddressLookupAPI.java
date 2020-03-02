package uk.gov.dvla.osl.addresslookup.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.core.Response;

@Api(value = "Address Lookup Service")
public interface AddressLookupAPI {

    @ApiOperation(
            value = "Get address list for postcode supplied",
            notes = "Get address list for postcode. \n \n Note: \nthe HTTP Header Param X-Correlation-Id is an optional value."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Valid response - with a list of addresses or an empty list if none are found"),
                    @ApiResponse(code = 400, message = "When an invalid request is received"),
                    @ApiResponse(code = 500, message = "Internal Server Error"
                    )
            })
    Response getAddressList(String postcode);

    @ApiOperation(
            value = "Match places using the address supplied",
            notes = "Match places using address. \n \n Note: \nthe HTTP Header Param X-Correlation-Id is an optional value."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Valid response - with a list of places or an empty list if none are found"),
                    @ApiResponse(code = 400, message = "When an invalid request is received"),
                    @ApiResponse(code = 500, message = "Internal Server Error"
                    )
            })
    Response matchAddress(String address);
}

package com.cc.database.jaxrs;


import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Controller
@Path("/imsdatabase/{version}/api/account/registration")
@Api(value = "Account Registration")
@SwaggerDefinition( consumes ={MediaType.APPLICATION_JSON}, produces = {MediaType.APPLICATION_JSON}, schemes = {
        SwaggerDefinition.Scheme.HTTP})
public interface RegistrationResource {
    @POST
    @Path("/verification")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Registration Verifcation", notes ="This method will verify user's info matches the records")
    @ApiResponse(value = { @ApiResponse( code = 200, message = "Successful", response = Response.class ),
            @ApiResponse(code = 401, message = "if invalid credentials are passed", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "On internal service failure", response = ErrorResponse.class) })
    Response verify (@Valid VerificationRequest verificationRequest);

}



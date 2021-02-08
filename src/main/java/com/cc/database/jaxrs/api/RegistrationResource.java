package com.cc.database.jaxrs.api;


import com.cc.database.jaxrs.api.model.ErrorResponse;
import com.cc.database.jaxrs.api.model.RegistrationOtpRequest;
import com.cc.database.jaxrs.api.model.VerificationRequest;
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
    @ApiOperation(value = "Registration Verification", notes ="This method will verify user's info matches the records")
    @ApiResponse(value = { @ApiResponse(code = 200, message = "Successful", response = Response.class),
            @ApiResponse(code = 401, message = "if invalid credentials are passed", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "On internal service failure", response = ErrorResponse.class) })
    Response verify(@Valid VerificationRequest verificationRequest);



    //cant figure out why the value is not picking up

    @POST
    @Path("/oneTimePassword")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Registration One Time Password Validation", notes ="This method validated that the one time password is legitimate")
    @ApiResponse(value = { @ApiResponse(code = 200, message = "Successful", response = Response.class),
            @ApiResponse(code = 400, message = "if one time password, email, and/or phone number is blank", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "if invalid one time password, email and/or phone number ", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "On internal service failure", response = ErrorResponse.class) })
    Response validateRegistrationOtp (@Valid RegistrationOtpRequest registrationOtpRequest);



}



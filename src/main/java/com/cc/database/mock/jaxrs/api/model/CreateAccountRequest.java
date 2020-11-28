package com.cc.database.mock.jaxrs.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Profile;

@Profile("mock")
@Getter
@Setter
@ApiModel(value= "Create Account Request model", description="A request to create an account" )
public class CreateAccountRequest {
    private String userId;
    private String password;
    private String emailAddress;
    private String householdPreference;
    private String emailPreference;
    private String phoneNumber;

}

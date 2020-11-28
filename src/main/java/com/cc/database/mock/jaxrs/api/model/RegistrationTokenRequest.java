package com.cc.database.mock.jaxrs.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Profile;

@Profile("mock")
@Getter
@Setter
@ApiModel(value = "Registration token request model ", description = "A request for a registration token ")
public class RegistrationTokenRequest {
    private String firstName;
    private String lastName;
    private String dob;
    private String memberId;
    private String email;
}

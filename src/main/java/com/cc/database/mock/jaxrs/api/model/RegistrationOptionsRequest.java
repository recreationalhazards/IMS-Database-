package com.cc.database.mock.jaxrs.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Profile;

@Profile("mock")
@Getter
@Setter
@ApiModel(value= "Validation Options model", description = "Registration options details")
public class RegistrationOptionsRequest {
    @ApiModelProperty(value = "First Name", required = true)
    private String firstName;
    @ApiModelProperty(value = "Last Name", required = true)
    private String lastName;
    @ApiModelProperty(value = "Date of Birth", required = true)
    private String dateOfBirth;


}

package com.cc.database.jaxrs.api.model;


import com.cc.database.mock.jaxrs.api.model.Mask;
import com.cc.database.validation.EmailValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ApiModel(value = "Registration Verification Request model", description = "Provide user name, email and activation info for registration Verification request")

public class VerificationRequest {
    @NotNull
    @Size(min = 2)
    @ApiModelProperty(value = "First name information", required = true, example = "John")
    private String firstName;

    @NotNull
    @Size (min = 2)
    @ApiModelProperty( value = "Last name information", required = true, example = "Doe")
    private String lastName;

    @Mask
    @NotNull
    @EmailValid
    @ApiModelProperty( value = "Email  information", required = true, example = "jdoe@test.com")
    private String emailAddress;

    @NotNull
    @Pattern(regexp = "\\d{10}")
    @ApiModelProperty( value = "phone number  information", required = true, example = "3123345678")
    private String phoneNumber;

    @ApiModelProperty(value = "One time password sent", required = true, example = "true")
    private Boolean needToSendOtp;

}

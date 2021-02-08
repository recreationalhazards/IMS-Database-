package com.cc.database.jaxrs.api.model;

import com.cc.database.mock.jaxrs.api.model.Mask;
import com.cc.database.validation.EmailValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ApiModel(value = "Registration One Time Password Request model", description = "Provide info for One time password request")
public class RegistrationOtpRequest {
    @Mask
    @NotBlank
    @ApiModelProperty(value = "One Time Password", required = true, example = "123456")
    private String oneTimePassword;


    @NotNull
    @Pattern(regexp = "\\d{10}")
    @ApiModelProperty(value = "Phone Number information", required = true, example = "1234567890")
    private String phoneNumber;

    @Mask
    @NotNull
    @EmailValid
    @ApiModelProperty(value = "Email Address", required= true, example = "jdoe@test.com")
    private String emailAddress;


}

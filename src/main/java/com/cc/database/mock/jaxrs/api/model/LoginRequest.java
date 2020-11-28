package com.cc.database.mock.jaxrs.api.model;


import com.cc.database.mock.validation.LoginRequestValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Profile;

@Profile("mock")
@LoginRequestValid
@Getter
@Setter
@ApiModel(value = "Login model", description = "Provide userId and password for login request")
public class LoginRequest {
    @ApiModelProperty(value = "UserId", required = true)
    private String userId;

    @ApiModelProperty(value = "Password", required = true)
    private String password;

    @ApiModelProperty(value = "DeviceId is required if user wants to login in with touch id.")
    private String deviceId;

    @ApiModelProperty(value = "DeviceCredentials is required if a user wants to login with touch id")
    private String deviceCredentials;


}

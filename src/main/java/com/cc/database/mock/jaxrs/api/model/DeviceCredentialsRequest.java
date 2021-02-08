package com.cc.database.mock.jaxrs.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Profile;

import org.hibernate.validator.constraints.NotEmpty;

@Profile("mock")
@Getter
@Setter
@ApiModel(value = "Touch Id login model", description = "Device Credential Request")
public class DeviceCredentialsRequest {
    @NotEmpty(message = "deviceId must be specified")
    @ApiModelProperty(value = "DeviceId", required = true)
    private String deviceId;

    @NotEmpty(message= "deviceDescription must be specified")
    @ApiModelProperty(value= "DeviceDescription", required = true)
    private String deviceDescription;

}

package com.cc.database.mock.jaxrs.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "Custom payload fields", description = "Specify the payload type of payload fields")
public class PayloadFields {
    @JsonProperty("PayloadType")
    private String payloadType;
}

package com.cc.database.jaxrs.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateAddressResponse {
    // add non nulll
    @JsonInclude(NON_NULL)
    @ApiModelProperty(value ="Error code")
    private Integer errorCode;

    @JsonInclude(NON_NULL)
    @ApiModelProperty(value ="Error message")
    private String errorMessage;

    @JsonInclude(NON_NULL)
    @ApiModelProperty(value =" Suggested Address")
    private StandardAddress suggestedAddress;








}

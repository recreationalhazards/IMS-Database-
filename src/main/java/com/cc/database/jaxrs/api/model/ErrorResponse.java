package com.cc.database.jaxrs.api.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
@ApiModel(value = "Error response model",description = "Successfully login response model")
public class ErrorResponse {
    @ApiModelProperty(value = "Response error code", required = true, example = "interger")
    private final ServiceErrorCode errorCode;
    @JsonInclude(NON_NULL)
    @ApiModelProperty(value = "Response error code description", required = true)
    @Getter
    private String errorMessage;

    public int getErrorCode(){return errorCode.getErrorCode();}

}

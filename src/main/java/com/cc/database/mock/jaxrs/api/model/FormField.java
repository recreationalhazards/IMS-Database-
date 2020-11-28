package com.cc.database.mock.jaxrs.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "Custom form field", description = "Specify the form field")
public class FormField {
    private String name;
    private String isToTokenize;
    private String value;
}

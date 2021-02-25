package com.cc.database.jaxrs.api.model;

import com.cc.database.backend.membership.model.address.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "AddressList model", description = "List of addresses")
public class AddressList {
    @ApiModelProperty(value ="Default address type ", required = true, example = "TEMPORARY", allowableValues = "TEMPORARY,HOUSEHOLD")
    private String defaultAddressType;
    @ApiModelProperty(value = "List of address", required = true)
    private List<Address> addresses = new ArrayList<>();
}

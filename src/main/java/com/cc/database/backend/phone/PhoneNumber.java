package com.cc.database.backend.phone;

import com.cc.database.backend.membership.model.Context;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PhoneNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    private String number;
    private String countryCallingCode;
    private String extension;
    private String lineOfBusiness;




}

package com.cc.database.validation;

import com.cc.database.jaxrs.api.model.AddressRequest;
import org.apache.commons.lang3.time.DateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressRequestValidator implements ConstraintValidator<AddressRequestValid, AddressRequest> {
    private static final String TEMPORARY_ADDRESS_TYPE ="TEMPORARY";

    @Override
    public void initialize (AddressRequestValid constraintAnnotation){
        //Do nothing
    }

    @Override
    public boolean isValid(AddressRequest addressRequest, ConstraintValidatorContext constraintValidatorContext){
        boolean isValidTempAddressDate = true;
        if((addressRequest.getAddress() == null) == (addressRequest.getStandard() == null)){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Exactly one of either standard or temporary address must be specified.")
                    .addConstraintViolation();
            return false;
        }
        if(isTemporaryAddresss(addressRequest)){
            isValidTempAddressDate = DateUtil.isValidDateRangeWithMinRange(addressRequest.getTempAddressStartDate(),
                    addressRequest.getTempAddressEndDate(), 14);
        }
    }
}

package com.cc.database.mock.validation;

import com.cc.database.mock.jaxrs.api.model.LoginRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Profile("mock")
public class LoginRequestValidator implements ConstraintValidator<LoginRequestValid, LoginRequest> {

    @Override
    public void initialize(LoginRequestValid contraintAnnotation){
        //This is just used for initialization

    }

    @Override
    public boolean isValid(LoginRequest loginRequest, ConstraintValidatorContext context){
        boolean isValid= false;
        boolean isValidDeviceId= isValidDeviceId(loginRequest.getDeviceId());
        boolean isValidDeviceCredentials= isValidDeviceCredentials(loginRequest.getDeviceCredentials());
         boolean isValidUserId= isValidUserId(loginRequest.getUserId());
         boolean isValidPassword = isValidPassword(loginRequest.getPassword());

         boolean isValidDeviceLogin = isValidDeviceId && isValidDeviceCredentials && !isValidUserId && !isValidPassword;
         boolean isValidUserLogin= isValidUserId && isValidPassword && !isValidDeviceId && !isValidDeviceCredentials;

         if(isValidDeviceLogin || isValidUserLogin){
             isValid = true;

         } else {
             context.disableDefaultConstraintViolation();
             context.buildConstraintViolationWithTemplate(
                     "A login request must contain either a device ID and device credentials, or a user ID and password.")
                     .addConstraintViolation();

         }
         return isValid;

    }

    private boolean isValidUserId(String userId){ return StringUtils.isNotBlank(userId);}

    private boolean isValidPassword(String password) { return StringUtils.isNotBlank(password);}

    private boolean isValidDeviceId(String deviceId){return StringUtils.isNotBlank(deviceId);}

    private boolean isValidDeviceCredentials(String deviceCredentials){
        return StringUtils.isNotBlank(deviceCredentials);

    }

}

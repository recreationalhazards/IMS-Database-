package com.cc.database.backend.service.api;

import com.cc.database.backend.membership.model.address.Address;
import com.cc.database.jaxrs.api.model.AddressList;
import com.cc.database.jaxrs.api.model.AddressRequest;
import com.cc.database.jaxrs.api.model.ValidateAddressResponse;

public interface AddressService {
    AddressList getAddress(String accessToken, String personNumber);
    Address addAddress( String accessToken, String personNumber, AddressRequest address);
    Address updateAddress(String accessToken, String personNumber, AddressRequest address);
    void deleteAddress( String accessToken, String personNumber, String addressType);
    ValidateAddressResponse validateAddress(String accessToken, String personNumber);
    void validateHouseholdAddress(String accessToken, String personNumber);
    CommonAddress getDefaultAddress (String accessToken, String personNumber);

}

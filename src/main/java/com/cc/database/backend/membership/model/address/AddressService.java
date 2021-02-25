package com.cc.database.backend.membership.model.address;

import com.cc.database.backend.service.api.AddressService;
import com.cc.database.jaxrs.api.model.AddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// Address Service is a business class that implements methods from AddressRepo
public class AddressServiceImp implements AddressService {

    @Autowired
    final private AddressRepo addressRepo;

    public AddressServiceImp(AddressRepo addressRepo) {
        this.addressRepo = addressRepo;
    }

    @Override
    public AddressList getAddress(String accessToken, String personNumber)  {
        return null;
    }

    @Override
    public Address addAddress(String accessToken, String personNumber, AddressRequest address) {
        return null;
    }

    @Override
    public Address updateAddress(String accessToken, String personNumber, AddressRequest address) {
        return null;
    }

    @Override
    public void deleteAddress(String accessToken, String personNumber, String addressType) {

    }

    @Override
    public ValidateAddressResponse validateAddress(String accessToken, String personNumber) {
        return null;
    }

    @Override
    public void validateHouseholdAddress(String accessToken, String personNumber) {

    }

    @Override
    public CommonAddress getDefaultAddress(String accessToken, String personNumber) {
        return null;
    }

//
//    // Find all Addresses
//    public Response getAllAddresses() {
//        List<Address> addressList = addressRepo.findAll();
//        if (addressList.size() > 0)
//            return Response.ok(addressList).build();
//        else
//            return Response.ok(new ArrayList<Address>()).build();
//    }
//
//    // Find Address by Id
//    public Response findAddressById(UUID uuid) {
//        Optional<Address> optionalAddress = addressRepo.findById(uuid);
//        if (optionalAddress.isPresent())
//            return Response.ok(optionalAddress.get()).build();
//        else
//            return Response.ok().build();
//    }
//
//    // Delete Address by Id
//    // Not sure if line 43 works
//    public Response deleteByAddress(UUID uuid) {
//        Optional<Address> optionalAddress = (Optional<Address>) findAddressById(uuid).getEntity();
//        if (optionalAddress.isPresent()) {
//            addressRepo.deleteById(uuid);
//            return Response.ok().build();
//        } else {
//            return Response.notModified().build();
//        }
//    }
//
//    // Update Address
//    public Response editAddress(Address address) {
//        Optional<Address> optionalAddress = addressRepo.findById(address.getResourceId());
//        if (optionalAddress.isPresent()) {
//            Address tempAddress = optionalAddress.get();
//            tempAddress.setStreet(address.getStreet());
//            tempAddress.setCity(address.getCity());
//            tempAddress.setState(address.getState());
//            tempAddress.setZipCode(address.getZipCode());
//            tempAddress.setCountry(address.getCountry());
//            addressRepo.save(tempAddress);
//            return Response.ok(tempAddress).build();
//        } else {
//            return Response.notModified(address.toString()).build();
//        }
//    }
}

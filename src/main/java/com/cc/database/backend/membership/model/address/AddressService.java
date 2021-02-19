package com.cc.database.backend.membership.model.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
// Address Service is a business class that implements methods from AddressRepo
public class AddressService {
    @Autowired
    final private AddressRepo addressRepo;

    public AddressService(AddressRepo addressRepo) {
        this.addressRepo = addressRepo;
    }

    // Find all Addresses
    public Response getAllAddresses() {
        List<Address> addressList = addressRepo.findAll();
        if (addressList.size() > 0)
            return Response.ok(addressList).build();
        else
            return Response.ok(new ArrayList<Address>()).build();
    }

    // Find Address by Id
    public Response findAddressById(UUID uuid) {
        Optional<Address> optionalAddress = addressRepo.findById(uuid);
        if (optionalAddress.isPresent())
            return Response.ok(optionalAddress.get()).build();
        else
            return Response.ok().build();
    }

    // Delete Address by Id
    // Not sure if line 43 works
    public Response deleteByAddress(UUID uuid) {
        Optional<Address> optionalAddress = (Optional<Address>) findAddressById(uuid).getEntity();
        if (optionalAddress.isPresent()) {
            addressRepo.deleteById(uuid);
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }

    // Update Address
    public Response editAddress(Address address) {
        Optional<Address> optionalAddress = addressRepo.findById(address.getResourceId());
        if (optionalAddress.isPresent()) {
            Address tempAddress = optionalAddress.get();
            tempAddress.setStreet(address.getStreet());
            tempAddress.setCity(address.getCity());
            tempAddress.setState(address.getState());
            tempAddress.setZipCode(address.getZipCode());
            tempAddress.setCountry(address.getCountry());
            addressRepo.save(tempAddress);
            return Response.ok(tempAddress).build();
        } else {
            return Response.notModified(address.toString()).build();
        }
    }
}

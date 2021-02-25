package com.cc.database.mock.config;

import com.cc.database.backend.service.api.AddressService;
import com.cc.database.mock.service.impl.MockAddressServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("mock")
@Configuration
@ComponentScan("com.cc.database ")
public class MockComponentConfiguration {

    @Bean
    public AddressService addressService(){return new MockAddressServiceImp(mockHandler());}



}

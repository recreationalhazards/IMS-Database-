package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.User;
import com.example.demo.persistence.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    UserLocation findByCountryAndUser(String country, User user);
}

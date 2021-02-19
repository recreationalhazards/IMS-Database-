package com.cc.database.backend.membership.model.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
// Address Repo uses Jpa to connect to database
public interface AddressRepo extends JpaRepository<Address, UUID> {
}

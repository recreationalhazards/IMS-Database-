package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    @Override
    void delete(Privilege privilege);

    Privilege findByName(String name);
}

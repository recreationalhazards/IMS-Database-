package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Override
    void delete(Role role);

    Role findByName(String name);
}

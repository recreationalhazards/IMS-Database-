package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    void delete(User user);

    User findByEmail(String email);
}

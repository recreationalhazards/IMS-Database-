package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {
}

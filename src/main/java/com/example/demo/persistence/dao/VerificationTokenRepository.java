package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.User;
import com.example.demo.persistence.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.stream.Stream;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {


    VerificationToken findByUser(User user);

    void deleteByExpiryDateLessThan(Date now);

    VerificationToken findByToken(String token);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

    Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);
}

package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.PasswordResetToken;
import com.example.demo.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.stream.Stream;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {


    PasswordResetToken findByUser(User user);

    void deleteByExpiryDateLessThan(Date now);

    PasswordResetToken findByToken(String token);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

}

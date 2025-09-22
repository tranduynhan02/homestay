package com.nhantd.homestay.repository;

import com.nhantd.homestay.model.ResetPassword;
import com.nhantd.homestay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, String> {
    Optional<ResetPassword> findByToken(String token);

    void deleteByUser(User user);
}

package com.credit.credit.repository;

import com.credit.credit.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findById(Long id);

    Optional<Client> findByPhoneNumber(String phone);
}

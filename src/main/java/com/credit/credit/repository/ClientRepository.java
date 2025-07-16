package com.credit.credit.repository;

import com.credit.credit.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findById(Long id);

    Optional<Client> findByPhoneNumber(String phone);
}

package com.convodesk.backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.convodesk.backend.Entity.Business;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByAssignedNumber(String assignedNumber);

}
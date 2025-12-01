package com.convodesk.backend.Repository;

import com.convodesk.backend.Entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findByBusinessId(Long businessId);
}

package com.project.repository;

import com.project.domain.SourceOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SourceOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceOrderRepository extends JpaRepository<SourceOrder, Long> {}

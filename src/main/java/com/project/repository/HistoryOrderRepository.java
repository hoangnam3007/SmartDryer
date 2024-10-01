package com.project.repository;

import com.project.domain.HistoryOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HistoryOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoryOrderRepository extends JpaRepository<HistoryOrder, Long> {}

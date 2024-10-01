package com.project.repository;

import com.project.domain.CustomerEquipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerEquipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerEquipmentRepository extends JpaRepository<CustomerEquipment, Long> {}

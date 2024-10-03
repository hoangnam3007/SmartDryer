package com.project.repository;

import com.project.domain.Staff;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Staff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {}

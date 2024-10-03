package com.project.repository;

import com.project.domain.Order;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(
        "SELECT o FROM Order o WHERE " +
        "(:provinceId IS NULL OR o.province.id = :provinceId) " +
        "AND (:districtId IS NULL OR o.district.id = :districtId) " +
        "AND (:wardId IS NULL OR o.ward.id = :wardId)"
    )
    Page<Order> findAllWithFilters(Pageable pageable, Long provinceId, Long districtId, Long wardId);

    // Find orders by staff id
    Page<Order> findByStaffId(Long staffId, Pageable pageable);

    // Check if there are any orders for a specific staff
    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.staff.id = :staffId")
    boolean existsByStaffId(@Param("staffId") Long staffId);
}

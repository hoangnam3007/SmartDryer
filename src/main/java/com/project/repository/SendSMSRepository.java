package com.project.repository;

import com.project.domain.SendSMS;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SendSMS entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SendSMSRepository extends JpaRepository<SendSMS, Long> {}

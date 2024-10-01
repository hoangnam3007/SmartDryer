package com.project.repository;

import com.project.domain.CusNote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CusNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CusNoteRepository extends JpaRepository<CusNote, Long> {}

package com.mvp.backend.infrastructure.repository;

import com.mvp.backend.domain.Enrolment;
import com.mvp.backend.domain.EnrolmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
    List<Enrolment> findByStudentId(Long studentId);

    Optional<Enrolment> findTopByStudentIdAndStatusOrderByCreatedAtDesc(Long studentId, EnrolmentStatus status);

    Optional<Enrolment> findTopByStudentIdOrderByCreatedAtDesc(Long studentId);

    List<Enrolment> findByStudentIdAndStatus(Long studentId, EnrolmentStatus status);
}

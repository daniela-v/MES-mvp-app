package com.mvp.backend.infrastructure.repository;

import com.mvp.backend.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

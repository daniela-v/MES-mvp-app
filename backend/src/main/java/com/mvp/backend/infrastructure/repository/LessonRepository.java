package com.mvp.backend.infrastructure.repository;

import com.mvp.backend.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseIdOrderBySequenceAsc(Long courseId);
}

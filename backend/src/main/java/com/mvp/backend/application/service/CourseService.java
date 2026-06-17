package com.mvp.backend.application.service;

import com.mvp.backend.api.dto.CourseResponse;
import com.mvp.backend.domain.Course;
import com.mvp.backend.infrastructure.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseResponse> getCourses() {
        return courseRepository.findAll().stream().map(this::toDto).toList();
    }

    public CourseResponse getCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        return toDto(course);
    }

    public Course getCourseEntity(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    private CourseResponse toDto(Course c) {
        return new CourseResponse(c.getId(), c.getSubject(), c.getSchoolYear(), c.getPrice());
    }
}

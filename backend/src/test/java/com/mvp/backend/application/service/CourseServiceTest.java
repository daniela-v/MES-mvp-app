package com.mvp.backend.application.service;

import com.mvp.backend.infrastructure.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock CourseRepository courseRepository;
    @InjectMocks CourseService courseService;

    @Test
    void getCoursesMapsEntitiesToResponses() {
        when(courseRepository.findAll()).thenReturn(List.of(TestData.course(1)));

        assertThat(courseService.getCourses()).singleElement().satisfies(course -> {
            assertThat(course.id()).isEqualTo(1);
            assertThat(course.subject()).isEqualTo("Maths");
        });
    }

    @Test
    void getCourseThrowsWhenCourseDoesNotExist() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourse(99L)).hasMessage("Course not found");
    }
}

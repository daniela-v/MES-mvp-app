package com.mvp.backend.application.service;

import com.mvp.backend.domain.Enrolment;
import com.mvp.backend.domain.EnrolmentStatus;
import com.mvp.backend.domain.StudentStatus;
import com.mvp.backend.infrastructure.repository.EnrolmentRepository;
import com.mvp.backend.infrastructure.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrolmentServiceTest {
    @Mock EnrolmentRepository enrolmentRepository;
    @Mock LessonRepository lessonRepository;
    @InjectMocks EnrolmentService enrolmentService;


    @Test
    void createPendingEnrolmentPersistsPendingEnrolmentForOrder() {
        var course = TestData.course(1);
        var student = TestData.student(2, StudentStatus.ACTIVE);
        var order = TestData.order(3, course);
        when(enrolmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Enrolment enrolment = enrolmentService.createPendingEnrolment(
                student, course, order, Instant.parse("2026-06-16T00:00:00Z"));

        assertThat(enrolment.getStudent()).isEqualTo(student);
        assertThat(enrolment.getCourse()).isEqualTo(course);
        assertThat(enrolment.getOrder()).isEqualTo(order);
        assertThat(enrolment.getStatus()).isEqualTo(EnrolmentStatus.PENDING);
        verify(enrolmentRepository).save(enrolment);
    }

    @Test
    void getStudentEnrolmentsReturnsStudentOwnedEnrolments() {
        var course = TestData.course(1);
        var student = TestData.student(2, StudentStatus.ACTIVE);
        when(enrolmentRepository.findByStudentId(2L)).thenReturn(List.of(TestData.enrolment(3, student, course)));

        assertThat(enrolmentService.getStudentEnrolments(2L)).singleElement().satisfies(enrolment -> {
            assertThat(enrolment.id()).isEqualTo(3);
            assertThat(enrolment.courseSubject()).isEqualTo("Maths");
        });
    }

    @Test
    void getLessonsRequiresEnrolmentOwnership() {
        var enrolment = TestData.enrolment(3, TestData.student(2, StudentStatus.ACTIVE), TestData.course(1));
        when(enrolmentRepository.findById(3L)).thenReturn(Optional.of(enrolment));

        assertThatThrownBy(() -> enrolmentService.getLessons(3L, 99L)).hasMessage("Enrolment not found");
    }

    @Test
    void getLessonRequiresLessonToBelongToEnrolmentCourse() {
        var course = TestData.course(1);
        var otherCourse = TestData.course(2);
        var enrolment = TestData.enrolment(3, TestData.student(2, StudentStatus.ACTIVE), course);
        when(enrolmentRepository.findById(3L)).thenReturn(Optional.of(enrolment));
        when(lessonRepository.findById(8L)).thenReturn(Optional.of(TestData.lesson(8, otherCourse)));

        assertThatThrownBy(() -> enrolmentService.getLesson(3L, 8L, 2L)).hasMessage("Lesson not found");
    }
}

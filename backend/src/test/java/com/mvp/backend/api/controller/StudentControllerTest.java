package com.mvp.backend.api.controller;

import com.mvp.backend.api.dto.EnrolmentResponse;
import com.mvp.backend.api.dto.StudentProfileResponse;
import com.mvp.backend.api.student.StudentController;
import com.mvp.backend.application.service.EnrolmentService;
import com.mvp.backend.application.service.StudentService;
import com.mvp.backend.security.AuthenticatedStudent;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentControllerTest {
    @Test
    void getStudentProfileRequiresAuthenticatedStudentToMatchPathStudent() {
        StudentService studentService = mock(StudentService.class);
        EnrolmentService enrolmentService = mock(EnrolmentService.class);
        when(studentService.getStudentProfile(7L)).thenReturn(new StudentProfileResponse(7L, "Ada", "ada@example.com", "ACTIVE", "Welcome back, Ada"));

        var response = new StudentController(enrolmentService, studentService)
                .getStudentProfile(7L, new AuthenticatedStudent(7L, "ada@example.com"));

        assertThat(response.dashboardName()).isEqualTo("Welcome back, Ada");
    }

    @Test
    void getStudentEnrolmentsRejectsOtherStudents() {
        var controller = new StudentController(mock(EnrolmentService.class), mock(StudentService.class));

        assertThatThrownBy(() -> controller.getStudentEnrolments(7L, new AuthenticatedStudent(8L, "other@example.com")))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void getStudentEnrolmentsReturnsOwnedEnrolments() {
        EnrolmentService enrolmentService = mock(EnrolmentService.class);
        when(enrolmentService.getStudentEnrolments(7L)).thenReturn(List.of(new EnrolmentResponse(1L, 2L, "Maths", "ACTIVE", Instant.now(), Instant.now())));

        var response = new StudentController(enrolmentService, mock(StudentService.class))
                .getStudentEnrolments(7L, new AuthenticatedStudent(7L, "ada@example.com"));

        assertThat(response).hasSize(1);
    }
}

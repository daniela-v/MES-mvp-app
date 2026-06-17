package com.mvp.backend.application.service;

import com.mvp.backend.domain.Student;
import com.mvp.backend.domain.StudentStatus;
import com.mvp.backend.infrastructure.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock StudentRepository studentRepository;
    @InjectMocks StudentService studentService;


    @Test
    void findOrCreateInvitedStudentCreatesInvitedStudentWhenMissing() {
        when(studentRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(studentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Student student = studentService.findOrCreateInvitedStudent("New Student", "new@example.com");

        assertThat(student.getName()).isEqualTo("New Student");
        assertThat(student.getEmail()).isEqualTo("new@example.com");
        assertThat(student.getStatus()).isEqualTo(StudentStatus.INVITED);
        verify(studentRepository).save(student);
    }

    @Test
    void findOrCreateInvitedStudentDoesNotOverwriteActiveStudentName() {
        Student active = TestData.student(7, StudentStatus.ACTIVE);
        when(studentRepository.findByEmail("ada@example.com")).thenReturn(Optional.of(active));
        when(studentRepository.save(active)).thenReturn(active);

        Student student = studentService.findOrCreateInvitedStudent("Different Name", "ada@example.com");

        assertThat(student.getName()).isEqualTo("Ada");
        assertThat(student.getStatus()).isEqualTo(StudentStatus.ACTIVE);
    }

    @Test
    void getStudentProfileReturnsDashboardName() {
        when(studentRepository.findById(7L)).thenReturn(Optional.of(TestData.student(7, StudentStatus.ACTIVE)));

        var profile = studentService.getStudentProfile(7L);

        assertThat(profile.dashboardName()).isEqualTo("Welcome back, Ada");
        assertThat(profile.status()).isEqualTo("ACTIVE");
    }
}

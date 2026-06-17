package com.mvp.backend.api.student;

import com.mvp.backend.api.dto.EnrolmentResponse;
import com.mvp.backend.api.dto.StudentProfileResponse;
import com.mvp.backend.application.service.EnrolmentService;
import com.mvp.backend.application.service.StudentService;
import com.mvp.backend.security.AuthenticatedStudent;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final EnrolmentService enrolmentService;
    private final StudentService studentService;

    public StudentController(EnrolmentService enrolmentService, StudentService studentService) {
        this.enrolmentService = enrolmentService;
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public StudentProfileResponse getStudentProfile(@PathVariable Long studentId,
                                                    @AuthenticationPrincipal AuthenticatedStudent authenticatedStudent) {
        assertCurrentStudent(studentId, authenticatedStudent);
        return studentService.getStudentProfile(studentId);
    }

    @GetMapping("/{studentId}/enrolments")
    public List<EnrolmentResponse> getStudentEnrolments(@PathVariable Long studentId,
                                                        @AuthenticationPrincipal AuthenticatedStudent authenticatedStudent) {
        assertCurrentStudent(studentId, authenticatedStudent);
        return enrolmentService.getStudentEnrolments(studentId);
    }

    private void assertCurrentStudent(Long studentId, AuthenticatedStudent authenticatedStudent) {
        if (authenticatedStudent == null || !studentId.equals(authenticatedStudent.id())) {
            throw new AccessDeniedException("Cannot access another student's resource");
        }
    }
}

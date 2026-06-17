package com.mvp.backend.application.service;

import com.mvp.backend.api.dto.StudentProfileResponse;
import com.mvp.backend.domain.Student;
import com.mvp.backend.domain.StudentStatus;
import com.mvp.backend.infrastructure.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student findOrCreateInvitedStudent(String name, String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseGet(() -> {
                    Student newStudent = new Student();
                    newStudent.setEmail(email);
                    newStudent.setStatus(StudentStatus.INVITED);
                    return newStudent;
                });

        if (student.getStatus() == StudentStatus.INVITED) {
            student.setName(name);
        }

        return studentRepository.save(student);
    }

    public boolean requiresOnboarding(Student student) {
        return student.getStatus() != StudentStatus.ACTIVE;
    }

    public StudentProfileResponse getStudentProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return new StudentProfileResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getStatus().name(),
                "Welcome back, " + student.getName()
        );
    }
}

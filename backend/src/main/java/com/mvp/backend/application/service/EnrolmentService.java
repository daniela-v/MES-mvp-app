package com.mvp.backend.application.service;

import com.mvp.backend.api.dto.EnrolmentResponse;
import com.mvp.backend.api.dto.LessonDetailResponse;
import com.mvp.backend.api.dto.LessonSummaryResponse;
import com.mvp.backend.domain.Course;
import com.mvp.backend.domain.Enrolment;
import com.mvp.backend.domain.EnrolmentStatus;
import com.mvp.backend.domain.Lesson;
import com.mvp.backend.domain.Order;
import com.mvp.backend.domain.Student;
import com.mvp.backend.infrastructure.repository.EnrolmentRepository;
import com.mvp.backend.infrastructure.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EnrolmentService {
    private final EnrolmentRepository enrolmentRepository;
    private final LessonRepository lessonRepository;

    public EnrolmentService(EnrolmentRepository enrolmentRepository, LessonRepository lessonRepository) {
        this.enrolmentRepository = enrolmentRepository;
        this.lessonRepository = lessonRepository;
    }

    public Enrolment createPendingEnrolment(Student student, Course course, Order order, Instant createdAt) {
        Enrolment enrolment = new Enrolment();
        enrolment.setStudent(student);
        enrolment.setCourse(course);
        enrolment.setOrder(order);
        enrolment.setCreatedAt(createdAt);
        enrolment.setValidUntil(createdAt.plus(365, ChronoUnit.DAYS));
        enrolment.setStatus(EnrolmentStatus.PENDING);
        return enrolmentRepository.save(enrolment);
    }

    public List<EnrolmentResponse> getStudentEnrolments(Long studentId) {
        return enrolmentRepository.findByStudentId(studentId).stream()
                .map(e -> new EnrolmentResponse(e.getId(), e.getCourse().getId(),
                        e.getCourse().getSubject(), e.getStatus().name(), e.getCreatedAt(), e.getValidUntil()))
                .toList();
    }

    public List<LessonSummaryResponse> getLessons(Long enrolmentId, Long studentId) {
        Enrolment enrolment = getAuthorizedEnrolment(enrolmentId, studentId);
        return lessonRepository.findByCourseIdOrderBySequenceAsc(enrolment.getCourse().getId()).stream()
                .map(l -> new LessonSummaryResponse(l.getId(), l.getTitle(), l.getSequence()))
                .toList();
    }

    public LessonDetailResponse getLesson(Long enrolmentId, Long lessonId, Long studentId) {
        Enrolment enrolment = getAuthorizedEnrolment(enrolmentId, studentId);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
        if (!lesson.getCourse().getId().equals(enrolment.getCourse().getId())) {
            throw new IllegalArgumentException("Lesson not found");
        }
        return new LessonDetailResponse(lesson.getId(), lesson.getTitle(), lesson.getContent(), lesson.getSequence());
    }

    private Enrolment getAuthorizedEnrolment(Long enrolmentId, Long studentId) {
        Enrolment enrolment = enrolmentRepository.findById(enrolmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrolment not found"));
        if (!enrolment.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("Enrolment not found");
        }
        return enrolment;
    }
}

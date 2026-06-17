package com.mvp.backend.application.service;

import com.mvp.backend.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;

final class TestData {
    private TestData() {}

    static Course course(long id) {
        Course course = new Course();
        ReflectionTestUtils.setField(course, "id", id);
        ReflectionTestUtils.setField(course, "subject", "Maths");
        ReflectionTestUtils.setField(course, "schoolYear", SchoolYear.YEAR_SEVEN);
        ReflectionTestUtils.setField(course, "price", BigDecimal.valueOf(100));
        return course;
    }

    static Student student(long id, StudentStatus status) {
        Student student = new Student();
        ReflectionTestUtils.setField(student, "id", id);
        student.setName("Ada");
        student.setEmail("ada@example.com");
        student.setStatus(status);
        return student;
    }

    static Order order(long id, Course course) {
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", id);
        order.setParentName("Parent");
        order.setParentEmail("parent@example.com");
        order.setCourse(course);
        order.setStudentName("Ada");
        order.setStudentEmail("student@example.com");
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setCreatedAt(Instant.parse("2026-06-16T00:00:00Z"));
        return order;
    }

    static Enrolment enrolment(long id, Student student, Course course) {
        Enrolment enrolment = new Enrolment();
        ReflectionTestUtils.setField(enrolment, "id", id);
        enrolment.setStudent(student);
        enrolment.setCourse(course);
        enrolment.setOrder(order(11, course));
        enrolment.setStatus(EnrolmentStatus.PENDING);
        enrolment.setCreatedAt(Instant.parse("2026-06-16T00:00:00Z"));
        enrolment.setValidUntil(Instant.parse("2027-06-16T00:00:00Z"));
        return enrolment;
    }

    static Lesson lesson(long id, Course course) {
        Lesson lesson = new Lesson();
        ReflectionTestUtils.setField(lesson, "id", id);
        ReflectionTestUtils.setField(lesson, "course", course);
        ReflectionTestUtils.setField(lesson, "title", "Lesson one");
        ReflectionTestUtils.setField(lesson, "content", "Lesson content");
        ReflectionTestUtils.setField(lesson, "sequence", 1);
        return lesson;
    }

    static Onboarding onboarding(long id, Student student) {
        Onboarding onboarding = new Onboarding();
        ReflectionTestUtils.setField(onboarding, "id", id);
        onboarding.setStudent(student);
        onboarding.setToken("token-123");
        onboarding.setExpiresAt(Instant.now().plusSeconds(3600));
        return onboarding;
    }
}

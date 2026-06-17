package com.mvp.backend.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "enrolments")
public class Enrolment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant validUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrolmentStatus status;

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public Order getOrder() { return order; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getValidUntil() { return validUntil; }
    public EnrolmentStatus getStatus() { return status; }

    public void setStudent(Student student) { this.student = student; }
    public void setCourse(Course course) { this.course = course; }
    public void setOrder(Order order) { this.order = order; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }
    public void setStatus(EnrolmentStatus status) { this.status = status; }
}

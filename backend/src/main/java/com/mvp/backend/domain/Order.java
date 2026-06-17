package com.mvp.backend.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String parentName;

    @Column(nullable = false)
    private String parentEmail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String studentEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false, unique = true)
    private String checkoutId;

    public Long getId() { return id; }
    public String getParentName() { return parentName; }
    public String getParentEmail() { return parentEmail; }
    public Course getCourse() { return course; }
    public String getStudentName() { return studentName; }
    public String getStudentEmail() { return studentEmail; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public String getCheckoutId() { return checkoutId; }

    public void setParentName(String parentName) { this.parentName = parentName; }
    public void setParentEmail(String parentEmail) { this.parentEmail = parentEmail; }
    public void setCourse(Course course) { this.course = course; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setCheckoutId(String checkoutId) { this.checkoutId = checkoutId; }
}

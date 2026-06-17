package com.mvp.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "school_year", nullable = false)
    private SchoolYear schoolYear;

    @Column(nullable = false)
    private BigDecimal price;

    public Long getId() { return id; }
    public String getSubject() { return subject; }
    public SchoolYear getSchoolYear() { return schoolYear; }
    public BigDecimal getPrice() { return price; }
}

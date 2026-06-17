package com.mvp.backend.api.controller;

import com.mvp.backend.api.dto.LessonDetailResponse;
import com.mvp.backend.api.dto.LessonSummaryResponse;
import com.mvp.backend.api.enrolment.EnrolmentController;
import com.mvp.backend.application.service.EnrolmentService;
import com.mvp.backend.security.AuthenticatedStudent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnrolmentControllerTest {
    @Test
    void getLessonsPassesJwtStudentIdToService() {
        EnrolmentService service = mock(EnrolmentService.class);
        when(service.getLessons(3L, 7L)).thenReturn(List.of(new LessonSummaryResponse(1L, "Intro", 1)));

        var response = new EnrolmentController(service).getLessons(3L, new AuthenticatedStudent(7L, "ada@example.com"));

        assertThat(response).singleElement().extracting(LessonSummaryResponse::title).isEqualTo("Intro");
    }

    @Test
    void getLessonPassesJwtStudentIdToService() {
        EnrolmentService service = mock(EnrolmentService.class);
        when(service.getLesson(3L, 1L, 7L)).thenReturn(new LessonDetailResponse(1L, "Intro", "Content", 1));

        var response = new EnrolmentController(service).getLesson(3L, 1L, new AuthenticatedStudent(7L, "ada@example.com"));

        assertThat(response.content()).isEqualTo("Content");
    }
}

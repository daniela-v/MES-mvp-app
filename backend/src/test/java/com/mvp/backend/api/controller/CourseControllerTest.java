package com.mvp.backend.api.controller;

import com.mvp.backend.api.course.CourseController;
import com.mvp.backend.api.dto.CourseResponse;
import com.mvp.backend.application.service.CourseService;
import com.mvp.backend.domain.SchoolYear;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CourseControllerTest {
    @Test
    void getCoursesDelegatesToService() {
        CourseService service = mock(CourseService.class);
        when(service.getCourses()).thenReturn(List.of(new CourseResponse(1L, "Maths", SchoolYear.YEAR_SEVEN, BigDecimal.TEN)));
        CourseController controller = new CourseController(service);

        assertThat(controller.getCourses()).singleElement().extracting(CourseResponse::subject).isEqualTo("Maths");
    }

    @Test
    void getCourseDelegatesToService() {
        CourseService service = mock(CourseService.class);
        when(service.getCourse(1L)).thenReturn(new CourseResponse(1L, "Maths", SchoolYear.YEAR_SEVEN, BigDecimal.TEN));
        CourseController controller = new CourseController(service);

        assertThat(controller.getCourse(1L).id()).isEqualTo(1L);
    }
}

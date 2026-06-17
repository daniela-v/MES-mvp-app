package com.mvp.backend.api.enrolment;

import com.mvp.backend.api.dto.LessonDetailResponse;
import com.mvp.backend.api.dto.LessonSummaryResponse;
import com.mvp.backend.application.service.EnrolmentService;
import com.mvp.backend.security.AuthenticatedStudent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class EnrolmentController {
    private final EnrolmentService enrolmentService;

    public EnrolmentController(EnrolmentService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    @GetMapping("/enrolments/{enrolmentId}/lessons")
    public List<LessonSummaryResponse> getLessons(@PathVariable Long enrolmentId,
                                                  @AuthenticationPrincipal AuthenticatedStudent student) {
        return enrolmentService.getLessons(enrolmentId, student.id());
    }

    @GetMapping("/enrolments/{enrolmentId}/lessons/{lessonId}")
    public LessonDetailResponse getLesson(@PathVariable Long enrolmentId,
                                          @PathVariable Long lessonId,
                                          @AuthenticationPrincipal AuthenticatedStudent student) {
        return enrolmentService.getLesson(enrolmentId, lessonId, student.id());
    }
}

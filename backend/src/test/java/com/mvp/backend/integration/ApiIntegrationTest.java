package com.mvp.backend.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvp.backend.api.dto.CompleteOnboardingRequest;
import com.mvp.backend.api.dto.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenSeedData_whenGetCourses_thenReturnsCatalog() throws Exception {
        mockMvc.perform(get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].subject").value("Maths"));
    }

    @Test
    void givenCourseId_whenGetCourse_thenReturnsCourse() throws Exception {
        mockMvc.perform(get("/api/v1/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("Maths"))
                .andExpect(jsonPath("$.schoolYear").value("YEAR_FIVE"));
    }

    @Test
    void givenInvalidOrderPayload_whenCreateOrder_thenReturnsBadRequest() throws Exception {
        CreateOrderRequest invalidRequest = new CreateOrderRequest("", "not-email", 1L, "", "student@example.com", "checkout-test");

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void givenValidPurchaseAndOnboarding_whenComplete_thenCanAccessProtectedLms() throws Exception {
        StudentSession session = createOnboardedStudent("student.one@example.com", 2L);

        mockMvc.perform(get("/api/v1/students/" + session.studentId() + "/enrolments")
                        .header("Authorization", "Bearer " + session.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].courseSubject").value("Science"));

        mockMvc.perform(get("/api/v1/students/" + session.studentId())
                        .header("Authorization", "Bearer " + session.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dashboardName").value("Welcome back, Student One"));

        mockMvc.perform(get("/api/v1/enrolments/" + session.enrolmentId() + "/lessons")
                        .header("Authorization", "Bearer " + session.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/api/v1/enrolments/" + session.enrolmentId() + "/lessons/3")
                        .header("Authorization", "Bearer " + session.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Scientific Method"));
    }

    @Test
    void givenNoJwt_whenAccessProtectedStudentResource_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/students/1/enrolments"))
                .andExpect(status().isForbidden());
    }

    private StudentSession createOnboardedStudent(String email, Long courseId) throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                "Parent One",
                "parent.one@example.com",
                courseId,
                "Student One",
                email,
                "checkout-" + email
        );

        MvcResult orderResult = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.onboardingRequired").value(true))
                .andExpect(jsonPath("$.accessUrl").exists())
                .andExpect(jsonPath("$.paymentStatus").doesNotExist())
                .andReturn();

        JsonNode orderJson = objectMapper.readTree(orderResult.getResponse().getContentAsString());
        String onboardingToken = orderJson.get("accessUrl").asText().replace("/onboarding?token=", "");

        mockMvc.perform(get("/api/v1/onboarding?token=" + onboardingToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentEmail").value(email));

        CompleteOnboardingRequest completeRequest = new CompleteOnboardingRequest(
                onboardingToken,
                "Student One",
                email,
                "StrongPass1!"
        );

        MvcResult onboardingResult = mockMvc.perform(post("/api/v1/onboarding")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(completeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andReturn();

        JsonNode onboardingJson = objectMapper.readTree(onboardingResult.getResponse().getContentAsString());
        String jwt = onboardingJson.get("token").asText();
        Long studentId = onboardingJson.get("studentId").asLong();

        MvcResult enrolmentsResult = mockMvc.perform(get("/api/v1/students/" + studentId + "/enrolments")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();

        Long enrolmentId = objectMapper
                .readTree(enrolmentsResult.getResponse().getContentAsString())
                .get(0)
                .get("id")
                .asLong();

        return new StudentSession(studentId, jwt, enrolmentId);
    }

    private record StudentSession(Long studentId, String jwt, Long enrolmentId) {
    }
}

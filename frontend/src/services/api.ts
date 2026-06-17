import { useAuthStore } from '../store/authStore';
import type {
  CompleteOnboardingRequest,
  CompleteOnboardingResponse,
  Course,
  CreateOrderRequest,
  CreateOrderResponse,
  Enrolment,
  LessonDetail,
  LessonSummary,
  OnboardingInfo,
  StudentProfile,
} from '../types/api';

const API_BASE = import.meta.env.VITE_API_BASE ?? 'http://localhost:8080/api/v1';

type ApiErrorBody = { message?: string; error?: string; detail?: string };

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const token = useAuthStore.getState().token;
  const headers = new Headers(options.headers);

  if (!headers.has('Content-Type') && options.body) {
    headers.set('Content-Type', 'application/json');
  }
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    if (response.status === 401 || response.status === 403) {
      useAuthStore.getState().clearAuth();
    }
    const body = (await response.json().catch(() => null)) as ApiErrorBody | null;
    throw new Error(body?.message || body?.detail || body?.error || `Request failed: ${response.status}`);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export const api = {
  getCourses: () => request<Course[]>('/courses'),
  getCourse: (id: string | number) => request<Course>(`/courses/${id}`),
  createOrder: (payload: CreateOrderRequest) =>
    request<CreateOrderResponse>('/orders', { method: 'POST', body: JSON.stringify(payload) }),
  getOnboarding: (token: string) => request<OnboardingInfo>(`/onboarding?token=${encodeURIComponent(token)}`),
  completeOnboarding: (payload: CompleteOnboardingRequest) =>
    request<CompleteOnboardingResponse>('/onboarding', { method: 'POST', body: JSON.stringify(payload) }),
  getStudentProfile: (studentId: string | number) => request<StudentProfile>(`/students/${studentId}`),
  getEnrolments: (studentId: string | number) => request<Enrolment[]>(`/students/${studentId}/enrolments`),
  getLessons: (enrolmentId: string | number) => request<LessonSummary[]>(`/enrolments/${enrolmentId}/lessons`),
  getLesson: (enrolmentId: string | number, lessonId: string | number) =>
    request<LessonDetail>(`/enrolments/${enrolmentId}/lessons/${lessonId}`),
};

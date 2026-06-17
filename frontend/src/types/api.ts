export type SchoolYear = 'YEAR_FIVE' | 'YEAR_SIX' | 'YEAR_SEVEN' | string;

export type Course = {
  id: number;
  subject: string;
  schoolYear: SchoolYear;
  price: number;
};

export type CreateOrderRequest = {
  parentName: string;
  parentEmail: string;
  courseId: number;
  studentName: string;
  studentEmail: string;
  checkoutId: string;
};

export type OrderStatus = 'PAID' | string;

export type CreateOrderResponse = {
  orderId: number;
  status: OrderStatus;
  onboardingRequired: boolean;
  accessUrl: string;
};

export type OnboardingInfo = {
  studentId: number;
  studentName: string;
  studentEmail: string;
  courseSubject: string;
  expiresAt: string;
  onboardingRequired: boolean;
};

export type CompleteOnboardingRequest = { token: string; name: string; email: string; password?: string };

export type CompleteOnboardingResponse = {
  studentId: number;
  status: string;
  token: string;
};

export type Enrolment = {
  id: number;
  courseId: number;
  courseSubject: string;
  status: string;
  createdAt: string;
  validUntil: string;
};

export type LessonSummary = { id: number; title: string; sequence: number };
export type LessonDetail = { id: number; title: string; content: string; sequence: number };

export type StudentProfile = { id: number; name: string; email: string; status: string; dashboardName: string };

import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { Layout } from './components/Layout';
import { ProtectedRoute } from './routes/ProtectedRoute';
import { PublicOnlyRoute } from './routes/PublicOnlyRoute';
import { CoursesPage } from './pages/CoursesPage';
import { CourseDetailPage } from './pages/CourseDetailPage';
import { CheckoutPage } from './pages/CheckoutPage';
import { OnboardingPage } from './pages/OnboardingPage';
import { DashboardPage } from './pages/DashboardPage';
import { EnrolmentDetailPage } from './pages/EnrolmentDetailPage';
import { LessonDetailPage } from './pages/LessonDetailPage';

function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<PublicOnlyRoute><CoursesPage /></PublicOnlyRoute>} />
          <Route path="/course/:id" element={<PublicOnlyRoute><CourseDetailPage /></PublicOnlyRoute>} />
          <Route path="/checkout/:courseId" element={<PublicOnlyRoute><CheckoutPage /></PublicOnlyRoute>} />
          <Route path="/onboarding" element={<OnboardingPage />} />
          <Route path="/dashboard" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />
          <Route path="/enrolment/:enrolmentId" element={<ProtectedRoute><EnrolmentDetailPage /></ProtectedRoute>} />
          <Route path="/enrolment/:enrolmentId/lesson/:lessonId" element={<ProtectedRoute><LessonDetailPage /></ProtectedRoute>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}

export default App;

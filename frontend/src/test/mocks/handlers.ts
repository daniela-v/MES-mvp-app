import { http, HttpResponse } from 'msw'

const apiBase = 'http://localhost:8080/api/v1'

export const orderRequests: unknown[] = []

export const resetTestState = () => {
  orderRequests.length = 0
}

export const handlers = [
  http.get(`${apiBase}/courses`, () => HttpResponse.json([
    { id: 1, subject: 'Maths', schoolYear: 'YEAR_FIVE', price: 199 },
    { id: 2, subject: 'Science', schoolYear: 'YEAR_SIX', price: 219 },
  ])),

  http.get(`${apiBase}/courses/:courseId`, ({ params }) => {
    const courseId = Number(params.courseId)
    return HttpResponse.json({
      id: courseId,
      subject: courseId === 2 ? 'Science' : 'Maths',
      schoolYear: courseId === 2 ? 'YEAR_SIX' : 'YEAR_FIVE',
      price: courseId === 2 ? 219 : 199,
    })
  }),

  http.post(`${apiBase}/orders`, async ({ request }) => {
    const body = await request.json()
    orderRequests.push(body)
    return HttpResponse.json({
      orderId: 6,
      status: 'PAID',
      onboardingRequired: true,
      accessUrl: '/onboarding?token=shared-token',
    })
  }),

  http.get(`${apiBase}/onboarding`, () => HttpResponse.json({
    studentId: 1,
    studentName: 'Tom Smith',
    studentEmail: 'tom@example.com',
    courseSubject: 'Maths',
    expiresAt: '2026-06-24T10:15:30Z',
    onboardingRequired: true,
  })),

  http.post(`${apiBase}/onboarding`, () => HttpResponse.json({
    studentId: 1,
    status: 'ACTIVE',
    token: 'jwt-token-here',
  })),

  http.get(`${apiBase}/students/:studentId`, ({ request }) => {
    if (!request.headers.get('Authorization')) {
      return HttpResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }
    return HttpResponse.json({
      id: 1,
      name: 'Tom Smith',
      email: 'tom@example.com',
      status: 'ACTIVE',
      dashboardName: 'Welcome back, Tom Smith',
    })
  }),

  http.get(`${apiBase}/students/:studentId/enrolments`, ({ request }) => {
    if (!request.headers.get('Authorization')) {
      return HttpResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }
    return HttpResponse.json([
      {
        id: 1,
        courseId: 2,
        courseSubject: 'Science',
        status: 'ACTIVE',
        createdAt: '2026-06-17T10:15:30Z',
        validUntil: '2027-06-17T10:15:30Z',
      },
    ])
  }),

  http.get(`${apiBase}/enrolments/:enrolmentId/lessons`, ({ request }) => {
    if (!request.headers.get('Authorization')) {
      return HttpResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }
    return HttpResponse.json([
      { id: 3, title: 'Scientific Method', sequence: 1 },
      { id: 4, title: 'Variables', sequence: 2 },
    ])
  }),

  http.get(`${apiBase}/enrolments/:enrolmentId/lessons/:lessonId`, ({ request }) => {
    if (!request.headers.get('Authorization')) {
      return HttpResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }
    return HttpResponse.json({
      id: 3,
      title: 'Scientific Method',
      content: 'Observation, hypothesis, experiments and results.',
      sequence: 1,
    })
  }),
]

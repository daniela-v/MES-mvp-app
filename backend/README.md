## Local development

Backend:

```bash
cd backend
mvn test
mvn spring-boot:run
```

Quickstart 

```bash
docker compose up --build
```

After the first build, start normally without rebuilding:

```bash
docker compose up
```

h2 db details:
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:mvpdb
Username: sa
Password: leave blank

# API Contracts

---

<details>
<summary><strong>1. GET /api/v1/courses</strong></summary>

**Returns:**

```json
[
  {
    "id": 1,
    "subject": "Maths",
    "schoolYear": "YEAR_FIVE",
    "price": 199.00
  }
]
```

</details>

---

<details>
<summary><strong>2. GET /api/v1/courses/{courseId}</strong></summary>

**Returns:**

```json
{
  "id": 2,
  "subject": "Science",
  "schoolYear": "YEAR_SIX",
  "price": 219.00
}
```

</details>

---

<details>
<summary><strong>3. POST /api/v1/orders</strong></summary>

**Request:**

```json
{
  "courseId": 1,
  "parentName": "Jane Smith",
  "parentEmail": "jane@example.com",
  "studentName": "Tom Smith",
  "studentEmail": "tom@example.com",
  "checkoutId": "browser-checkout-session-id"
}
```

**Response — onboarding required:**

```json
{
  "orderId": 883,
  "status": "PAID",
  "onboardingRequired": true,
  "accessUrl": "/onboarding?token=994i2844-i63f-85h8-e150-880099884444"
}
```

**Response — onboarding not required:**

```json
{
  "orderId": 884,
  "status": "PAID",
  "onboardingRequired": false,
  "accessUrl": "/onboarding?token=994i2844-i63f-85h8-e150-880099884444"
}
```

</details>

---

<details>
<summary><strong>4. GET /api/v1/onboarding?token={token}</strong></summary>

**Returns:**

```json
{
  "studentId": 1,
  "studentName": "Tom Smith",
  "studentEmail": "tom@example.com",
  "checkoutId": "browser-checkout-session-id",
  "courseSubject": "Maths",
  "expiresAt": "2026-06-24T10:15:30Z",
  "onboardingRequired": true
}
```

</details>

---

<details>
<summary><strong>5. POST /api/v1/onboarding</strong></summary>

**Request:**

```json
{
  "token": "994i2844-i63f-85h8-e150-880099884444",
  "name": "Tom Smith",
  "email": "tom@example.com",
  "password": "StrongPass1!"
}
```

**Response:**

```json
{
  "studentId": 1,
  "status": "ACTIVE",
  "token": "jwt-token-here"
}
```

</details>

---

<details>
<summary><strong>6. GET /api/v1/students/{studentId}</strong></summary>

> Requires `Authorization: Bearer {jwt}`

**Returns:**

```json
{
  "id": 1,
  "name": "Tom Smith",
  "email": "tom@example.com",
  "status": "ACTIVE",
  "dashboardName": "Welcome back, Tom Smith"
}
```

</details>

---

<details>
<summary><strong>7. GET /api/v1/students/{studentId}/enrolments</strong></summary>

> Requires `Authorization: Bearer {jwt}`

**Returns:**

```json
[
  {
    "id": 1,
    "courseId": 2,
    "courseSubject": "Science",
    "status": "ACTIVE",
    "createdAt": "2026-06-17T10:15:30Z",
    "validUntil": "2027-06-17T10:15:30Z"
  }
]
```

</details>

---

<details>
<summary><strong>8. GET /api/v1/enrolments/{enrolmentId}/lessons</strong></summary>

> Requires `Authorization: Bearer {jwt}`

**Returns:**

```json
[
  {
    "id": 3,
    "title": "Scientific Method",
    "sequence": 1
  }
]
```

</details>

---

<details>
<summary><strong>9. GET /api/v1/enrolments/{enrolmentId}/lessons/{lessonId}</strong></summary>

> Requires `Authorization: Bearer {jwt}`

**Returns:**

```json
{
  "id": 3,
  "title": "Scientific Method",
  "content": "Observation, hypothesis, experiments and results.",
  "sequence": 1
}
```

</details>

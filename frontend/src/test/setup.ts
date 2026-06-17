import '@testing-library/jest-dom/vitest'
import { afterAll, afterEach, beforeAll, vi } from 'vitest'
import { server } from './mocks/server'
import { resetTestState } from './mocks/handlers'
import { useAuthStore } from '../store/authStore'

beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))

afterEach(() => {
  server.resetHandlers()
  resetTestState()
  localStorage.clear()
  useAuthStore.getState().clearAuth()
  vi.restoreAllMocks()
})

afterAll(() => server.close())

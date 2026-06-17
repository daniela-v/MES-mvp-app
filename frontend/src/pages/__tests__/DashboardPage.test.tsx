import { screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import { useAuthStore } from '../../store/authStore'
import { renderAppAt } from '../../test/renderApp'

describe('DashboardPage', () => {
  it('redirects to courses when unauthenticated', async () => {
    renderAppAt('/dashboard')

    expect(await screen.findByRole('heading', { name: /choose a course/i })).toBeInTheDocument()
    expect(window.location.pathname).toBe('/')
  })

  it('loads student profile and enrolments when authenticated', async () => {
    useAuthStore.getState().setAuth(1, 'jwt-token-here')
    renderAppAt('/dashboard')

    expect(await screen.findByText('Welcome back, Tom Smith')).toBeInTheDocument()
    expect(screen.getByText('Science')).toBeInTheDocument()
  })
})

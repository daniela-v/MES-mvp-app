import { screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, expect, it } from 'vitest'
import { renderAppAt } from '../../test/renderApp'

describe('OnboardingPage', () => {
  it('submits onboarding and stores the JWT and student id', async () => {
    const user = userEvent.setup()
    renderAppAt('/onboarding?token=shared-token')

    expect(await screen.findByDisplayValue('Tom Smith')).toBeInTheDocument()
    expect(screen.getByDisplayValue('tom@example.com')).toBeInTheDocument()

    await user.type(screen.getByLabelText(/create password/i), 'StrongPass1!')
    await user.click(screen.getByRole('button', { name: /activate and enter lms/i }))

    expect(await screen.findByText('Welcome back, Tom Smith')).toBeInTheDocument()
    expect(localStorage.getItem('jwtToken')).toBe('jwt-token-here')
    expect(localStorage.getItem('studentId')).toBe('1')
  })
})

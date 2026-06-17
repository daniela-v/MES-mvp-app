import { screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import { useAuthStore } from '../../store/authStore'
import { renderAppAt } from '../../test/renderApp'

describe('Lessons', () => {
  it('renders the lesson list for an enrolment', async () => {
    useAuthStore.getState().setAuth(1, 'jwt-token-here')
    renderAppAt('/enrolment/1')

    expect(await screen.findByText('1. Scientific Method')).toBeInTheDocument()
    expect(screen.getByText('2. Variables')).toBeInTheDocument()
  })

  it('renders lesson detail content', async () => {
    useAuthStore.getState().setAuth(1, 'jwt-token-here')
    renderAppAt('/enrolment/1/lesson/3')

    expect(await screen.findByRole('heading', { name: 'Scientific Method' })).toBeInTheDocument()
    expect(screen.getByText('Observation, hypothesis, experiments and results.')).toBeInTheDocument()
  })
})

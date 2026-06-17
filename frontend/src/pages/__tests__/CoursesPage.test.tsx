import { screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, expect, it } from 'vitest'
import { renderAppAt } from '../../test/renderApp'

describe('CoursesPage', () => {
  it('renders courses and navigates to checkout from the buy button', async () => {
    const user = userEvent.setup()
    renderAppAt('/')

    expect(await screen.findByText('Maths')).toBeInTheDocument()
    expect(screen.getByText('Science')).toBeInTheDocument()

    await user.click(screen.getAllByRole('link', { name: /buy/i })[0])

    expect(await screen.findByRole('heading', { name: /checkout/i })).toBeInTheDocument()
    expect(window.location.pathname).toBe('/checkout/1')
  })
})

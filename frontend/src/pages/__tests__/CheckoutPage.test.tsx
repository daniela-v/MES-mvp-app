import { screen, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, expect, it } from 'vitest'
import { orderRequests } from '../../test/mocks/handlers'
import { renderAppAt } from '../../test/renderApp'

describe('CheckoutPage', () => {
  it('submits one order and opens the shared access link returned by checkout', async () => {
    const user = userEvent.setup()
    renderAppAt('/checkout/1')

    await screen.findByRole('heading', { name: /checkout/i })
    await user.type(screen.getByLabelText(/parent name/i), 'Jane Smith')
    await user.type(screen.getByLabelText(/parent email/i), 'jane@example.com')
    await user.type(screen.getByLabelText(/student name/i), 'Tom Smith')
    await user.type(screen.getByLabelText(/student email/i), 'tom@example.com')

    const submit = screen.getByRole('button', { name: /create order/i })
    await user.dblClick(submit)

    const dialog = await screen.findByRole('dialog')
    expect(within(dialog).getByText(/share or open the onboarding link below/i)).toBeInTheDocument()
    expect(within(dialog).getByDisplayValue('http://localhost:3000/onboarding?token=shared-token')).toBeInTheDocument()
    expect(orderRequests).toHaveLength(1)
    expect(orderRequests[0]).toMatchObject({
      courseId: 1,
      parentName: 'Jane Smith',
      parentEmail: 'jane@example.com',
      studentName: 'Tom Smith',
      studentEmail: 'tom@example.com',
    })
  })
})

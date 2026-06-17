import { renderWithProviders } from './render'
import App from '../App'

export function renderAppAt(path: string) {
  window.history.pushState({}, 'Test page', path)
  return renderWithProviders(<App />)
}

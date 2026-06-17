import { createTheme } from '@mui/material';

export const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#3157d5' },
    secondary: { main: '#00a896' },
    background: { default: '#f5f7fb', paper: '#ffffff' },
  },
  shape: { borderRadius: 16 },
  typography: {
    fontFamily: 'Inter, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif',
    h4: { fontWeight: 800 },
    h5: { fontWeight: 750 },
    h6: { fontWeight: 700 },
  },
  components: {
    MuiButton: {
      defaultProps: { disableElevation: true },
      styleOverrides: { root: { textTransform: 'none', fontWeight: 700 } },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          border: '1px solid rgba(49, 87, 213, 0.08)',
          boxShadow: '0 16px 45px rgba(27, 39, 94, 0.08)',
        },
      },
    },
  },
});

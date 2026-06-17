import { AppBar, Box, Button, Container, Stack, Toolbar, Typography } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { layoutSx } from '../styles';
import { useAuthStore } from '../store/authStore';

export function Layout({ children }: { children: React.ReactNode }) {
  const navigate = useNavigate();
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const clearAuth = useAuthStore((state) => state.clearAuth);
  const homePath = isAuthenticated ? '/dashboard' : '/';

  const handleLogout = () => {
    clearAuth();
    navigate('/');
  };

  return (
    <Box sx={layoutSx.page}>
      <AppBar position="sticky" color="transparent" elevation={0} sx={layoutSx.appBar}>
        <Toolbar>
          <Stack direction="row" spacing={1.25} sx={layoutSx.brandLink} component={Link} to={homePath}>
            <Box sx={layoutSx.brandMark}>
              <Typography variant="h6" sx={layoutSx.brandText}>L</Typography>
            </Box>
            <Box>
              <Typography variant="h6" sx={layoutSx.brandText}>MVP LMS</Typography>
              <Typography variant="caption" color="text.secondary">Parent checkout to student learning</Typography>
            </Box>
          </Stack>
          <Stack direction="row" spacing={1}>
            {!isAuthenticated && <Button component={Link} to="/">Courses</Button>}
            {isAuthenticated && <Button component={Link} to="/dashboard">Dashboard</Button>}
            {isAuthenticated && <Button onClick={handleLogout} variant="outlined">Logout</Button>}
          </Stack>
        </Toolbar>
      </AppBar>
      <Container maxWidth="lg" sx={layoutSx.container}>
        {children}
      </Container>
    </Box>
  );
}

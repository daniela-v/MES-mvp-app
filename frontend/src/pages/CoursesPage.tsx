import { Alert, Box, Button, Card, CardActions, CardContent, Skeleton, Stack, Typography } from '@mui/material';
import { Link } from 'react-router-dom';
import { useAsyncData } from '../hooks/useAsyncData';
import { api } from '../services/api';
import { commonSx } from '../styles';
import { formatCurrency, formatSchoolYear } from '../utils';

export function CoursesPage() {
  const { data: courses, error, loading } = useAsyncData(() => api.getCourses(), []);

  return (
    <Stack spacing={3}>
      <Box sx={commonSx.heroText}>
        <Typography variant="h4" gutterBottom>Choose a course</Typography>
      </Box>
      {error && <Alert severity="error">{error}</Alert>}
      {loading && <Skeleton variant="rounded" height={220} />}
      <Box sx={commonSx.responsiveGrid}>
        {(courses ?? []).map((course) => (
          <Card key={course.id} sx={commonSx.fillCard}>
            <CardContent sx={commonSx.grow}>
              <Stack spacing={1.25}>
                <Typography variant="h5">{course.subject}</Typography>
                <Typography color="text.secondary">{formatSchoolYear(course.schoolYear)}</Typography>
                <Typography variant="h5" color="primary.main">{formatCurrency(course.price)}</Typography>
                <Typography color="text.secondary">Description should come from the db we don't have this right now.</Typography>
              </Stack>
            </CardContent>
            <CardActions sx={commonSx.cardActions}>
              <Button component={Link} to={`/course/${course.id}`} fullWidth>Details</Button>
              <Button component={Link} to={`/checkout/${course.id}`} variant="contained" fullWidth>Buy</Button>
            </CardActions>
          </Card>
        ))}
      </Box>
    </Stack>
  );
}

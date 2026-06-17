import { Alert, Box, Button, Card, CardContent, Divider, Skeleton, Stack, Typography } from '@mui/material';
import { Link, useParams } from 'react-router-dom';
import { useAsyncData } from '../hooks/useAsyncData';
import { api } from '../services/api';
import { commonSx } from '../styles';
import { formatCurrency, formatSchoolYear } from '../utils';

export function CourseDetailPage() {
  const { id = '' } = useParams();
  const { data: course, error, loading } = useAsyncData(() => api.getCourse(id), [id]);

  if (error) return <Alert severity="error">{error}</Alert>;
  if (loading || !course) return <Skeleton variant="rounded" height={260} />;

  return (
    <Card>
      <CardContent sx={commonSx.spaciousCardContent}>
        <Stack spacing={2.5}>
          <Box>
            <Typography variant="h4">{course.subject}</Typography>
            <Typography color="text.secondary" sx={commonSx.topSpacing}>{formatSchoolYear(course.schoolYear)}</Typography>
          </Box>
          <Divider />
          <Typography variant="h4" color="primary.main">{formatCurrency(course.price)}</Typography>
          <Typography color="text.secondary">Description should come from the db we don't have this right now.</Typography>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5}>
            <Button component={Link} to={`/checkout/${course.id}`} variant="contained" size="large">Start checkout</Button>
            <Button component={Link} to="/" size="large">Back to courses</Button>
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  );
}

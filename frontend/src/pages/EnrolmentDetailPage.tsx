import { Alert, Card, CardContent, Chip, List, ListItemButton, ListItemText, Skeleton, Stack, Typography } from '@mui/material';
import { Link, useParams } from 'react-router-dom';
import { useAsyncData } from '../hooks/useAsyncData';
import { api } from '../services/api';
import { commonSx } from '../styles';

export function EnrolmentDetailPage() {
  const { enrolmentId = '' } = useParams();
  const { data: lessons, error, loading } = useAsyncData(() => api.getLessons(enrolmentId), [enrolmentId]);

  return (
    <Card>
      <CardContent sx={commonSx.cardContent}>
        <Stack spacing={2}>
          <Chip label="Protected LMS" color="secondary" sx={commonSx.badge} />
          <Typography variant="h4">Lessons</Typography>
          {error && <Alert severity="error">{error}</Alert>}
          {loading && <Skeleton variant="rounded" height={180} />}
          <List disablePadding>
            {(lessons ?? []).map((lesson) => (
              <ListItemButton component={Link} key={lesson.id} to={`/enrolment/${enrolmentId}/lesson/${lesson.id}`} sx={commonSx.listItem}>
                <ListItemText primary={`${lesson.sequence}. ${lesson.title}`} secondary="Open lesson" />
              </ListItemButton>
            ))}
          </List>
        </Stack>
      </CardContent>
    </Card>
  );
}

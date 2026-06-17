import { Alert, Button, Card, CardContent, Chip, Skeleton, Stack, Typography } from '@mui/material';
import { Link, useParams } from 'react-router-dom';
import { useAsyncData } from '../hooks/useAsyncData';
import { api } from '../services/api';
import { commonSx } from '../styles';

export function LessonDetailPage() {
  const { enrolmentId = '', lessonId = '' } = useParams();
  const { data: lesson, error, loading } = useAsyncData(() => api.getLesson(enrolmentId, lessonId), [enrolmentId, lessonId]);

  if (error) return <Alert severity="error">{error}</Alert>;
  if (loading || !lesson) return <Skeleton variant="rounded" height={260} />;

  return (
    <Card>
      <CardContent sx={commonSx.spaciousCardContent}>
        <Stack spacing={2}>
          <Chip label={`Lesson ${lesson.sequence}`} color="secondary" sx={commonSx.badge} />
          <Typography variant="h4">{lesson.title}</Typography>
          <Typography sx={commonSx.lessonContent}>{lesson.content}</Typography>
          <Button component={Link} to={`/enrolment/${enrolmentId}`} sx={commonSx.startAligned}>Back to lessons</Button>
        </Stack>
      </CardContent>
    </Card>
  );
}

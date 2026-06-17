import { Alert, Box, Card, CardContent, Chip, List, ListItemButton, ListItemText, Stack, Typography } from '@mui/material';
import { Link } from 'react-router-dom';
import { useAsyncData } from '../hooks/useAsyncData';
import { api } from '../services/api';
import { useAuthStore } from '../store/authStore';
import { commonSx } from '../styles';
import { formatDate } from '../utils';

export function DashboardPage() {
  const studentId = useAuthStore((state) => state.studentId);
  const { data, error } = useAsyncData(
    async () => {
      if (!studentId) return null;
      const [profile, enrolments] = await Promise.all([api.getStudentProfile(studentId), api.getEnrolments(studentId)]);
      return { profile, enrolments };
    },
    [studentId],
  );

  const profile = data?.profile ?? null;
  const enrolments = data?.enrolments ?? [];

  return (
    <Stack spacing={3}>
      <Card>
        <CardContent sx={commonSx.cardContent}>
          <Stack spacing={1}>
            <Typography variant="h4">{profile?.dashboardName ?? 'Student dashboard'}</Typography>
            <Typography color="text.secondary">Your LMS dashboard, active course enrolments and lesson access.</Typography>
            {profile && <Chip label={profile.email} sx={commonSx.startAligned} />}
          </Stack>
        </CardContent>
      </Card>

      {!studentId && <Alert severity="error">Student session is missing. Please complete onboarding again.</Alert>}
      {error && <Alert severity="error">{error}</Alert>}

      <Card>
        <CardContent>
          <Box sx={commonSx.sectionHeader}>
            <Typography variant="h6">My courses</Typography>
            <Typography color="text.secondary">Select an enrolment to view available lessons.</Typography>
          </Box>
          {enrolments.length === 0 && <Alert severity="info">No active enrolments found.</Alert>}
          <List disablePadding>
            {enrolments.map((enrolment) => (
              <ListItemButton key={enrolment.id} component={Link} to={`/enrolment/${enrolment.id}`} sx={commonSx.listItem}>
                <ListItemText
                  primary={<Stack direction="row" spacing={1} sx={commonSx.inlineActions}><span>{enrolment.courseSubject}</span><Chip size="small" label={enrolment.status} color="secondary" /></Stack>}
                  secondary={`Valid until ${formatDate(enrolment.validUntil)}`}
                />
              </ListItemButton>
            ))}
          </List>
        </CardContent>
      </Card>
    </Stack>
  );
}

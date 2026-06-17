import { Alert, Button, Card, CardContent, Stack, TextField, Typography } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import type { ChangeEvent, FormEvent } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { api } from '../services/api';
import { useAuthStore } from '../store/authStore';
import { commonSx } from '../styles';
import type { OnboardingInfo } from '../types/api';
import { formatDate } from '../utils';

const initialForm = { name: '', email: '', password: '' };

export function OnboardingPage() {
  const [params] = useSearchParams();
  const token = useMemo(() => params.get('token') || '', [params]);
  const [info, setInfo] = useState<OnboardingInfo | null>(null);
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();
  const setAuth = useAuthStore((s) => s.setAuth);

  useEffect(() => {
    let active = true;
    if (!token) return undefined;

    api.getOnboarding(token)
      .then((data) => {
        if (!active) return;
        setInfo(data);
        setForm((prev) => ({ ...prev, name: data.studentName, email: data.studentEmail }));
      })
      .catch((err) => {
        if (active) setError((err as Error).message);
      });

    return () => {
      active = false;
    };
  }, [token]);

  const updateField = (field: keyof typeof initialForm) => (event: ChangeEvent<HTMLInputElement>) => {
    setForm((current) => ({ ...current, [field]: event.target.value }));
  };

  const finishAccess = async (payload = form) => {
    setError('');
    setSubmitting(true);
    try {
      const result = await api.completeOnboarding({ token, ...payload });
      setAuth(result.studentId, result.token);
      navigate('/dashboard');
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setSubmitting(false);
    }
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    await finishAccess(form);
  };

  const handleExistingStudentAccess = () => finishAccess({ name: info?.studentName ?? '', email: info?.studentEmail ?? '', password: '' });

  return (
    <Card sx={commonSx.narrowCard}>
      <CardContent sx={commonSx.cardContent}>
        <Typography variant="h4">Student access</Typography>
        {info && (
          <Alert severity="info" sx={commonSx.topSpacing}>
            Access for {info.courseSubject}. Link expires {formatDate(info.expiresAt)}.
          </Alert>
        )}

        {info?.onboardingRequired ? (
          <Stack component="form" spacing={2} sx={commonSx.formSpacing} onSubmit={handleSubmit}>
            <TextField label="Student name" required value={form.name} onChange={updateField('name')} />
            <TextField label="Student email" type="email" required value={form.email} onChange={updateField('email')} />
            <TextField label="Create password" type="password" required slotProps={{ htmlInput: { minLength: 6 } }} value={form.password} onChange={updateField('password')} />
            <Button variant="contained" size="large" type="submit" disabled={submitting || !token}>{submitting ? 'Activating…' : 'Activate and enter LMS'}</Button>
          </Stack>
        ) : info ? (
          <Stack spacing={2} sx={commonSx.formSpacing}>
            <Typography color="text.secondary">This student already has an active account. Use this link to open their dashboard.</Typography>
            <Button variant="contained" size="large" onClick={handleExistingStudentAccess} disabled={submitting || !token}>{submitting ? 'Opening…' : 'Enter LMS'}</Button>
          </Stack>
        ) : null}

        {!token && <Alert severity="error" sx={commonSx.topSpacing}>Missing access token. Use the link from checkout.</Alert>}
        {error && <Alert severity="error" sx={commonSx.topSpacing}>{error}</Alert>}
      </CardContent>
    </Card>
  );
}

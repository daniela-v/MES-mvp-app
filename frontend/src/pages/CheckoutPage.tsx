import { Alert, Box, Button, Card, CardContent, Dialog, DialogActions, DialogContent, DialogTitle, Divider, Stack, TextField, Typography } from '@mui/material';
import { useRef, useState } from 'react';
import type { ChangeEvent, FormEvent } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useAsyncData } from '../hooks/useAsyncData';
import { api } from '../services/api';
import { commonSx } from '../styles';
import type { CreateOrderResponse } from '../types/api';
import { formatCurrency, formatSchoolYear } from '../utils';

type CheckoutForm = {
  parentName: string;
  parentEmail: string;
  studentName: string;
  studentEmail: string;
};

const initialForm: CheckoutForm = {
  parentName: '',
  parentEmail: '',
  studentName: '',
  studentEmail: '',
};

function createCheckoutId() {
  return crypto.randomUUID();
}

function absoluteAccessUrl(accessUrl: string) {
  return new URL(accessUrl, window.location.origin).toString();
}

export function CheckoutPage() {
  const { courseId = '' } = useParams();
  const { data: course, error: courseError } = useAsyncData(() => api.getCourse(courseId), [courseId]);
  const [form, setForm] = useState<CheckoutForm>(initialForm);
  const [checkoutId, setCheckoutId] = useState(createCheckoutId);
  const [order, setOrder] = useState<CreateOrderResponse | null>(null);
  const [copied, setCopied] = useState(false);
  const [submitError, setSubmitError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const submittingRef = useRef(false);
  const [accessDialogOpen, setAccessDialogOpen] = useState(false);

  const updateField = (field: keyof CheckoutForm) => (event: ChangeEvent<HTMLInputElement>) => {
    setForm((current) => ({ ...current, [field]: event.target.value }));
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    if (submittingRef.current || order) return;

    setCopied(false);
    setSubmitError('');
    submittingRef.current = true;
    setSubmitting(true);

    try {
      const result = await api.createOrder({
        parentName: form.parentName.trim(),
        parentEmail: form.parentEmail.trim(),
        studentName: form.studentName.trim(),
        studentEmail: form.studentEmail.trim(),
        courseId: Number(courseId),
        checkoutId,
      });
      setOrder(result);
      setAccessDialogOpen(Boolean(result.accessUrl));
    } catch (err) {
      setSubmitError((err as Error).message);
    } finally {
      submittingRef.current = false;
      setSubmitting(false);
    }
  };

  const startNewCheckout = () => {
    setForm(initialForm);
    setCheckoutId(createCheckoutId());
    setOrder(null);
    setCopied(false);
    setSubmitError('');
    setAccessDialogOpen(false);
  };

  const relativeAccessUrl = order?.accessUrl ?? '';
  const accessLink = relativeAccessUrl ? absoluteAccessUrl(relativeAccessUrl) : '';

  const copyAccessLink = async () => {
    if (!accessLink) return;
    await navigator.clipboard.writeText(accessLink);
    setCopied(true);
  };

  return (
    <>
      <Card sx={commonSx.checkoutCard}>
        <CardContent sx={commonSx.cardContent}>
          <Stack spacing={0.75} sx={commonSx.sectionHeader}>
            <Typography variant="h4">Checkout</Typography>
            <Typography color="text.secondary">Enter the parent and student details to create the order.</Typography>
          </Stack>

          {course && (
            <Box sx={commonSx.courseSummary}>
              <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1} sx={commonSx.splitRow}>
                <Box>
                  <Typography variant="h6">{course.subject}</Typography>
                  <Typography color="text.secondary">{formatSchoolYear(course.schoolYear)}</Typography>
                </Box>
                <Typography variant="h5" color="primary.main">{formatCurrency(course.price)}</Typography>
              </Stack>
            </Box>
          )}

          <Stack component="form" spacing={2.25} onSubmit={handleSubmit}>
            <Typography variant="h6">Parent details</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField label="Parent name" required fullWidth value={form.parentName} onChange={updateField('parentName')} disabled={Boolean(order)} />
              <TextField label="Parent email" type="email" required fullWidth value={form.parentEmail} onChange={updateField('parentEmail')} disabled={Boolean(order)} />
            </Stack>

            <Divider />
            <Typography variant="h6">Student details</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField label="Student name" required fullWidth value={form.studentName} onChange={updateField('studentName')} disabled={Boolean(order)} />
              <TextField label="Student email" type="email" required fullWidth value={form.studentEmail} onChange={updateField('studentEmail')} disabled={Boolean(order)} />
            </Stack>

            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5}>
              <Button type="submit" variant="contained" size="large" disabled={submitting || Boolean(order)}>
                {submitting ? 'Creating order…' : order ? `Order #${order.orderId} created` : 'Create order'}
              </Button>
              {order && <Button size="large" onClick={startNewCheckout}>Start new checkout</Button>}
            </Stack>
          </Stack>

          {courseError && <Alert severity="error" sx={commonSx.topSpacing}>{courseError}</Alert>}
          {submitError && <Alert severity="error" sx={commonSx.topSpacing}>{submitError}</Alert>}
        </CardContent>
      </Card>

      <Dialog open={accessDialogOpen} onClose={() => setAccessDialogOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>Order #{order?.orderId} is {order?.status.toLowerCase()}</DialogTitle>
        <DialogContent>
          <Stack spacing={2}>
            <Typography>Share or open the onboarding link below.</Typography>
            <TextField value={accessLink} fullWidth label="Student access link" slotProps={{ input: { readOnly: true } }} />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={copyAccessLink}>{copied ? 'Copied' : 'Copy link'}</Button>
          {relativeAccessUrl && <Button component={Link} to={relativeAccessUrl} variant="contained">Open link</Button>}
        </DialogActions>
      </Dialog>
    </>
  );
}

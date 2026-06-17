import type { SxProps, Theme } from '@mui/material/styles';

export const layoutSx = {
  page: {
    minHeight: '100vh',
    background: 'linear-gradient(180deg, #eef3ff 0%, #f8fafc 42%, #f5f7fb 100%)',
  },
  appBar: {
    backdropFilter: 'blur(14px)',
    borderBottom: '1px solid rgba(49, 87, 213, 0.1)',
  },
  brandLink: {
    alignItems: 'center',
    flexGrow: 1,
    color: 'inherit',
    textDecoration: 'none',
  },
  brandMark: {
    display: 'grid',
    placeItems: 'center',
    width: 38,
    height: 38,
    borderRadius: 3,
    bgcolor: 'primary.main',
    color: 'white',
  },
  brandText: { lineHeight: 1 },
  container: { py: { xs: 3, md: 5 } },
} satisfies Record<string, SxProps<Theme>>;

export const commonSx = {
  narrowCard: { maxWidth: 720, mx: 'auto' },
  checkoutCard: { maxWidth: 920, mx: 'auto' },
  cardContent: { p: { xs: 3, md: 4 } },
  spaciousCardContent: { p: { xs: 3, md: 5 } },
  sectionHeader: { mb: 3 },
  heroText: { maxWidth: 780 },
  responsiveGrid: {
    display: 'grid',
    gridTemplateColumns: { xs: '1fr', md: 'repeat(3, 1fr)' },
    gap: 2.5,
  },
  fillCard: { height: '100%', display: 'flex', flexDirection: 'column' },
  grow: { flexGrow: 1 },
  cardActions: { px: 2, pb: 2 },
  courseSummary: {
    p: 2,
    mb: 3,
    borderRadius: 3,
    bgcolor: 'action.hover',
    border: 1,
    borderColor: 'divider',
  },
  splitRow: {
    justifyContent: 'space-between',
    alignItems: { sm: 'center' },
  },
  inlineActions: { alignItems: 'center' },
  topSpacing: { mt: 2 },
  formSpacing: { mt: 3 },
  badge: { alignSelf: 'flex-start', fontWeight: 700 },
  listItem: {
    borderRadius: 2,
    mb: 1,
    border: '1px solid rgba(49, 87, 213, 0.1)',
  },
  lessonContent: { mt: 1, whiteSpace: 'pre-wrap', fontSize: '1.08rem', lineHeight: 1.8 },
  startAligned: { alignSelf: 'flex-start' },
  strong: { fontWeight: 700 },
} satisfies Record<string, SxProps<Theme>>;

import { create } from 'zustand';

type AuthState = {
  studentId: number | null;
  token: string | null;
  isAuthenticated: boolean;
  setAuth: (studentId: number, token: string) => void;
  clearAuth: () => void;
};

const initialStudentId = Number(localStorage.getItem('studentId') || 0) || null;
const initialToken = localStorage.getItem('jwtToken');

export const useAuthStore = create<AuthState>((set) => ({
  studentId: initialStudentId,
  token: initialToken,
  isAuthenticated: Boolean(initialToken),
  setAuth: (studentId, token) => {
    localStorage.setItem('studentId', String(studentId));
    localStorage.setItem('jwtToken', token);
    set({ studentId, token, isAuthenticated: true });
  },
  clearAuth: () => {
    localStorage.removeItem('studentId');
    localStorage.removeItem('jwtToken');
    set({ studentId: null, token: null, isAuthenticated: false });
  },
}));

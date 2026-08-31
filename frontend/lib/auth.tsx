'use client';

import { createContext, useCallback, useContext, useEffect, useState } from 'react';
import { api, TOKEN_KEY } from './api';
import type { AuthResponse, UserSummary } from './types';

interface AuthContextValue {
  user: UserSummary | null;
  ready: boolean;
  login: (email: string, password: string) => Promise<AuthResponse>;
  registerCandidate: (data: Record<string, unknown>) => Promise<AuthResponse>;
  registerCompany: (data: Record<string, unknown>) => Promise<AuthResponse>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

function readStoredUser(): UserSummary | null {
  if (typeof window === 'undefined') return null;
  const raw = sessionStorage.getItem(TOKEN_KEY);
  try { return raw ? (JSON.parse(raw) as AuthResponse).user : null; } catch { sessionStorage.removeItem(TOKEN_KEY); return null; }
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserSummary | null>(readStoredUser);
  const [ready] = useState(() => typeof window !== 'undefined');

  const restore = useCallback(() => {
    setUser(readStoredUser());
  }, []);

  useEffect(() => {
    window.addEventListener('vertice:unauthorized', restore);
    return () => window.removeEventListener('vertice:unauthorized', restore);
  }, [restore]);

  const save = (session: AuthResponse) => {
    sessionStorage.setItem(TOKEN_KEY, JSON.stringify(session));
    setUser(session.user);
    return session;
  };
  const login = async (email: string, password: string) => save((await api.post<AuthResponse>('/auth/login', { email, password })).data);
  const registerCandidate = async (data: Record<string, unknown>) => save((await api.post<AuthResponse>('/auth/register/candidate', data)).data);
  const registerCompany = async (data: Record<string, unknown>) => save((await api.post<AuthResponse>('/auth/register/company', data)).data);
  const logout = () => { sessionStorage.removeItem(TOKEN_KEY); setUser(null); };

  return <AuthContext.Provider value={{ user, ready, login, registerCandidate, registerCompany, logout }}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const value = useContext(AuthContext);
  if (!value) throw new Error('useAuth deve ser usado dentro de AuthProvider');
  return value;
}

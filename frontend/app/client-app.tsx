'use client';

import { useSyncExternalStore } from 'react';
import { BrowserRouter, Navigate, Route, Routes, useLocation } from 'react-router-dom';
import { ShieldAlert } from 'lucide-react';
import { useAuth } from '@/lib/auth';
import type { Role } from '@/lib/types';
import { AppShell } from '@/components/app/app-shell';
import { AppPage, EmptyState } from '@/components/app/common';
import { LandingPage } from '@/features/landing-page';
import { LoginPage, RegisterPage } from '@/features/auth-pages';
import { JobDetailPage, JobsPage } from '@/features/jobs-pages';
import { CandidateApplicationsPage, CandidateDashboardPage, CandidateProfilePage, FavoritesPage, RecommendedPage } from '@/features/candidate-pages';
import { CompanyDashboardPage, CompanyJobsPage, CompanyProfilePage, JobApplicationsPage, JobFormPage } from '@/features/company-pages';

function Protected({ requiredRole, children }: { requiredRole: Role; children: React.ReactNode }) {
  const { user, ready } = useAuth(); const location = useLocation();
  if (!ready) return <div className="grid min-h-[60vh] place-items-center text-sm text-muted-foreground">Preparando sua área…</div>;
  if (!user) return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  if (user.role !== requiredRole) return <Navigate to="/forbidden" replace />;
  return children;
}

function ForbiddenPage() { return <AppPage><div className="grid min-h-[55vh] place-items-center"><div className="text-center"><ShieldAlert className="mx-auto size-10 text-amber-500" /><h1 className="mt-5 text-3xl font-semibold">Acesso não permitido</h1><p className="mt-2 text-muted-foreground">Esta área pertence a outro tipo de perfil.</p></div></div></AppPage>; }
function NotFoundPage() { return <AppPage><EmptyState title="Página não encontrada" description="O endereço pode ter mudado ou não existe." actionLabel="Voltar ao início" actionTo="/" /></AppPage>; }

export function ClientApp() {
  const mounted = useSyncExternalStore(() => () => undefined, () => true, () => false);
  if (!mounted) return <div className="min-h-screen bg-background" />;
  return <BrowserRouter><Routes><Route element={<AppShell />}><Route index element={<LandingPage />} /><Route path="jobs" element={<JobsPage />} /><Route path="jobs/:id" element={<JobDetailPage />} /><Route path="login" element={<LoginPage />} /><Route path="register" element={<RegisterPage />} /><Route path="candidate/dashboard" element={<Protected requiredRole="CANDIDATE"><CandidateDashboardPage /></Protected>} /><Route path="candidate/recommended" element={<Protected requiredRole="CANDIDATE"><RecommendedPage /></Protected>} /><Route path="candidate/applications" element={<Protected requiredRole="CANDIDATE"><CandidateApplicationsPage /></Protected>} /><Route path="candidate/favorites" element={<Protected requiredRole="CANDIDATE"><FavoritesPage /></Protected>} /><Route path="candidate/profile" element={<Protected requiredRole="CANDIDATE"><CandidateProfilePage /></Protected>} /><Route path="company/dashboard" element={<Protected requiredRole="COMPANY"><CompanyDashboardPage /></Protected>} /><Route path="company/jobs" element={<Protected requiredRole="COMPANY"><CompanyJobsPage /></Protected>} /><Route path="company/jobs/new" element={<Protected requiredRole="COMPANY"><JobFormPage /></Protected>} /><Route path="company/jobs/:id/edit" element={<Protected requiredRole="COMPANY"><JobFormPage /></Protected>} /><Route path="company/jobs/:id/applications" element={<Protected requiredRole="COMPANY"><JobApplicationsPage /></Protected>} /><Route path="company/profile" element={<Protected requiredRole="COMPANY"><CompanyProfilePage /></Protected>} /><Route path="forbidden" element={<ForbiddenPage />} /><Route path="*" element={<NotFoundPage />} /></Route></Routes></BrowserRouter>;
}

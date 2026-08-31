import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { api } from './api';
import type { Application, ApplicationStatus, CandidateDashboard, CandidateProfile, CompanyDashboard, CompanyProfile, Favorite, Job, JobFilters, JobPayload, Match, Page, Recommended } from './types';

export function useJobs(filters: JobFilters = {}) {
  return useQuery({ queryKey: ['jobs', filters], queryFn: () => api.get<Page<Job>>('/jobs', { params: { ...filters, sort: 'createdAt,desc' } }).then(r => r.data) });
}
export function useJob(id?: string) { return useQuery({ queryKey: ['job', id], queryFn: () => api.get<Job>(`/jobs/${id}`).then(r => r.data), enabled: Boolean(id) }); }
export function useMatch(id?: string, enabled = false) { return useQuery({ queryKey: ['match', id], queryFn: () => api.get<Match>(`/jobs/${id}/match`).then(r => r.data), enabled: Boolean(id) && enabled }); }
export function useCandidateDashboard() { return useQuery({ queryKey: ['candidate-dashboard'], queryFn: () => api.get<CandidateDashboard>('/candidates/me/dashboard').then(r => r.data) }); }
export function useCompanyDashboard() { return useQuery({ queryKey: ['company-dashboard'], queryFn: () => api.get<CompanyDashboard>('/companies/me/dashboard').then(r => r.data) }); }
export function useCandidateProfile() { return useQuery({ queryKey: ['candidate-profile'], queryFn: () => api.get<CandidateProfile>('/candidates/me/profile').then(r => r.data) }); }
export function useCompanyProfile() { return useQuery({ queryKey: ['company-profile'], queryFn: () => api.get<CompanyProfile>('/companies/me/profile').then(r => r.data) }); }
export function useFavorites() { return useQuery({ queryKey: ['favorites'], queryFn: () => api.get<Favorite[]>('/candidates/me/favorites').then(r => r.data) }); }
export function useRecommended() { return useQuery({ queryKey: ['recommended'], queryFn: () => api.get<Recommended[]>('/candidates/me/recommended-jobs').then(r => r.data) }); }
export function useCandidateApplications() { return useQuery({ queryKey: ['candidate-applications'], queryFn: () => api.get<Application[]>('/candidates/me/applications').then(r => r.data) }); }
export function useCompanyJobs() { return useQuery({ queryKey: ['company-jobs'], queryFn: () => api.get<Job[]>('/companies/me/jobs').then(r => r.data) }); }
export function useJobApplications(id?: string | number) { return useQuery({ queryKey: ['job-applications', id], queryFn: () => api.get<Application[]>(`/jobs/${id}/applications`).then(r => r.data), enabled: Boolean(id) }); }

export function useApiMutation<TData = unknown, TVariables = void>(fn: (variables: TVariables) => Promise<TData>, invalidates: string[][] = []) {
  const client = useQueryClient();
  return useMutation({ mutationFn: fn, onSuccess: () => invalidates.forEach((key) => client.invalidateQueries({ queryKey: key })) });
}
export const mutations = {
  apply: (jobId: number, coverLetter: string) => api.post<Application>(`/jobs/${jobId}/applications`, { coverLetter }).then(r => r.data),
  favorite: (jobId: number) => api.post<Favorite>(`/jobs/${jobId}/favorite`).then(r => r.data),
  unfavorite: (jobId: number) => api.delete(`/jobs/${jobId}/favorite`),
  cancelApplication: (id: number) => api.delete(`/applications/${id}`),
  updateStatus: (id: number, status: ApplicationStatus) => api.patch<Application>(`/applications/${id}/status`, { status }).then(r => r.data),
  updateCandidate: (data: Record<string, unknown>) => api.put<CandidateProfile>('/candidates/me/profile', data).then(r => r.data),
  updateCompany: (data: Record<string, unknown>) => api.put<CompanyProfile>('/companies/me/profile', data).then(r => r.data),
  createJob: (data: JobPayload) => api.post<Job>('/jobs', data).then(r => r.data),
  updateJob: (id: number, data: JobPayload) => api.put<Job>(`/jobs/${id}`, data).then(r => r.data),
  closeJob: (id: number) => api.patch(`/jobs/${id}/close`),
  deleteJob: (id: number) => api.delete(`/jobs/${id}`),
};

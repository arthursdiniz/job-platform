import type { ApplicationStatus, EmploymentType, ExperienceLevel } from './types';

export const levelLabels: Record<ExperienceLevel, string> = { INTERN: 'Estágio', JUNIOR: 'Júnior', MID_LEVEL: 'Pleno', SENIOR: 'Sênior' };
export const employmentLabels: Record<EmploymentType, string> = { FULL_TIME: 'Tempo integral', PART_TIME: 'Meio período', CONTRACT: 'Contrato', INTERNSHIP: 'Estágio' };
export const statusLabels: Record<ApplicationStatus, string> = { APPLIED: 'Enviada', UNDER_REVIEW: 'Em análise', INTERVIEW: 'Entrevista', REJECTED: 'Não selecionada', ACCEPTED: 'Aprovada' };
export const money = (value?: number) => value == null ? null : new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL', maximumFractionDigits: 0 }).format(value);
export const date = (value: string) => new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: 'short', year: 'numeric' }).format(new Date(value));
export const location = (item: { remote: boolean; city?: string; state?: string }) => item.remote ? 'Remoto' : [item.city, item.state].filter(Boolean).join(', ') || 'Local a combinar';
export const initials = (name: string) => name.split(/\s+/).slice(0, 2).map((part) => part[0]).join('').toUpperCase();

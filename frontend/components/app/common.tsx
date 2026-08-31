'use client';

import { Link } from 'react-router-dom';
import { AlertCircle, ArrowRight, BriefcaseBusiness, Building2, CalendarDays, MapPin, Search } from 'lucide-react';
import type { ApplicationStatus, Job } from '@/lib/types';
import { date, employmentLabels, levelLabels, location, money, statusLabels } from '@/lib/format';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { Empty, EmptyContent, EmptyDescription, EmptyHeader, EmptyMedia, EmptyTitle } from '@/components/ui/empty';
import { Skeleton } from '@/components/ui/skeleton';
import { cn } from '@/lib/utils';
import { NumberTicker } from '@/components/ui/number-ticker';

export function PageHeader({ eyebrow, title, description, action }: { eyebrow?: string; title: string; description?: string; action?: React.ReactNode }) {
  return <div className="mb-7 flex flex-col justify-between gap-4 sm:flex-row sm:items-end"><div>{eyebrow && <p className="mb-2 text-xs font-semibold uppercase tracking-[.18em] text-primary">{eyebrow}</p>}<h1 className="text-3xl font-semibold tracking-tight sm:text-4xl">{title}</h1>{description && <p className="mt-2 max-w-2xl text-muted-foreground">{description}</p>}</div>{action}</div>;
}
export function AppPage({ children }: { children: React.ReactNode }) { return <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 sm:py-10">{children}</div>; }
export function StatCard({ label, value, icon: Icon, tone = 'primary' }: { label: string; value: number; icon: React.ElementType; tone?: 'primary' | 'amber' | 'green' }) {
  return <Card className="shadow-sm"><CardContent className="flex items-center gap-4 p-5"><span className={cn('grid size-11 place-items-center rounded-lg', tone === 'amber' ? 'bg-amber-500/12 text-amber-600' : tone === 'green' ? 'bg-emerald-500/12 text-emerald-600' : 'bg-primary/10 text-primary')}><Icon className="size-5" /></span><div><p className="text-2xl font-semibold"><NumberTicker value={value} className="text-foreground" /></p><p className="text-sm text-muted-foreground">{label}</p></div></CardContent></Card>;
}
export function StatusBadge({ status }: { status: ApplicationStatus }) {
  const classes: Record<ApplicationStatus, string> = { APPLIED: 'bg-sky-500/12 text-sky-700 dark:text-sky-300', UNDER_REVIEW: 'bg-amber-500/14 text-amber-700 dark:text-amber-300', INTERVIEW: 'bg-violet-500/12 text-violet-700 dark:text-violet-300', REJECTED: 'bg-rose-500/12 text-rose-700 dark:text-rose-300', ACCEPTED: 'bg-emerald-500/12 text-emerald-700 dark:text-emerald-300' };
  return <Badge className={cn('border-0', classes[status])}>{statusLabels[status]}</Badge>;
}
export function JobCard({ job, aside }: { job: Job; aside?: React.ReactNode }) {
  const salary = money(job.salaryMin);
  return <Card className="group transition-all hover:-translate-y-0.5 hover:border-primary/35 hover:shadow-lg"><CardContent className="p-5 sm:p-6"><div className="flex gap-4"><span className="grid size-11 shrink-0 place-items-center rounded-lg border bg-secondary text-primary"><Building2 className="size-5" /></span><div className="min-w-0 flex-1"><div className="flex items-start justify-between gap-3"><div><Link to={`/jobs/${job.id}`} className="text-lg font-semibold tracking-tight group-hover:text-primary">{job.title}</Link><p className="mt-0.5 text-sm text-muted-foreground">{job.company.companyName}</p></div>{aside}</div><div className="mt-4 flex flex-wrap gap-x-4 gap-y-2 text-xs text-muted-foreground"><span className="flex items-center gap-1.5"><MapPin className="size-3.5" />{location(job)}</span><span className="flex items-center gap-1.5"><BriefcaseBusiness className="size-3.5" />{employmentLabels[job.employmentType]}</span><span className="flex items-center gap-1.5"><CalendarDays className="size-3.5" />{date(job.createdAt)}</span>{salary && <span className="font-medium text-foreground">A partir de {salary}</span>}</div><div className="mt-4 flex flex-wrap items-center gap-2">{job.skills.slice(0, 4).map(skill => <Badge key={skill} variant="secondary">{skill}</Badge>)}<Badge variant="outline">{levelLabels[job.experienceLevel]}</Badge></div></div></div></CardContent></Card>;
}
export function LoadingCards({ count = 3 }: { count?: number }) { return <div className="grid gap-4">{Array.from({ length: count }).map((_, i) => <Skeleton key={i} className="h-44 rounded-xl" />)}</div>; }
export function EmptyState({ title, description, actionLabel, actionTo }: { title: string; description: string; actionLabel?: string; actionTo?: string }) { return <Empty className="min-h-64 border"><EmptyHeader><EmptyMedia variant="icon"><Search /></EmptyMedia><EmptyTitle>{title}</EmptyTitle><EmptyDescription>{description}</EmptyDescription></EmptyHeader>{actionLabel && actionTo && <EmptyContent><Button nativeButton={false} render={<Link to={actionTo} />}>{actionLabel}<ArrowRight /></Button></EmptyContent>}</Empty>; }
export function ErrorState({ message = 'Não foi possível carregar os dados.', retry }: { message?: string; retry?: () => void }) { return <div className="flex min-h-48 flex-col items-center justify-center gap-3 rounded-xl border border-destructive/30 bg-destructive/5 p-8 text-center"><AlertCircle className="size-6 text-destructive" /><p className="text-sm text-muted-foreground">{message}</p>{retry && <Button variant="outline" onClick={retry}>Tentar novamente</Button>}</div>; }

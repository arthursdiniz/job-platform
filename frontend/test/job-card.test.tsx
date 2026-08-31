import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it } from 'vitest';
import { JobCard } from '@/components/app/common';
import type { Job } from '@/lib/types';

const job: Job = { id: 1, title: 'Desenvolvedor Java', description: 'API', company: { id: 1, companyName: 'Acme' }, city: 'São Paulo', state: 'SP', country: 'Brasil', remote: false, employmentType: 'FULL_TIME', experienceLevel: 'MID_LEVEL', salaryMin: 8000, salaryMax: 11000, status: 'OPEN', skills: ['Java', 'Spring Boot'], createdAt: '2026-08-30T12:00:00Z', updatedAt: '2026-08-30T12:00:00Z', expiresAt: '2026-10-30T12:00:00Z' };

describe('JobCard', () => {
  it('exibe dados reais da vaga e link para detalhes', () => {
    render(<MemoryRouter><JobCard job={job} /></MemoryRouter>);
    expect(screen.getByRole('link', { name: 'Desenvolvedor Java' })).toHaveAttribute('href', '/jobs/1');
    expect(screen.getByText('Acme')).toBeInTheDocument();
    expect(screen.getByText('Java')).toBeInTheDocument();
  });
});

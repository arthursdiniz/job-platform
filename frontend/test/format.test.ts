import { describe, expect, it } from 'vitest';
import { employmentLabels, levelLabels, location, money, statusLabels } from '@/lib/format';

describe('formatadores da interface', () => {
  it('traduz enums de domínio', () => {
    expect(levelLabels.SENIOR).toBe('Sênior');
    expect(employmentLabels.FULL_TIME).toBe('Tempo integral');
    expect(statusLabels.UNDER_REVIEW).toBe('Em análise');
  });
  it('prioriza remoto na localização', () => expect(location({ remote: true, city: 'Recife' })).toBe('Remoto'));
  it('formata valores em reais', () => expect(money(8500)).toContain('8.500'));
});

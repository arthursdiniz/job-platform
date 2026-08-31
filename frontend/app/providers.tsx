'use client';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { AuthProvider } from '@/lib/auth';
import { Toaster } from '@/components/ui/toast';

export function ClientProviders({ children }: { children: React.ReactNode }) {
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: { staleTime: 30_000, retry: 1, refetchOnWindowFocus: false },
        },
      }),
  );

  useEffect(() => {
    document.documentElement.classList.toggle('dark', localStorage.getItem('vertice.theme') === 'dark');
  }, []);

  return <QueryClientProvider client={queryClient}><AuthProvider><Toaster>{children}</Toaster></AuthProvider></QueryClientProvider>;
}

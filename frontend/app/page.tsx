import { ClientProviders } from './providers';
import { ClientApp } from './client-app';

export default function Home() {
  return (
    <ClientProviders>
      <ClientApp />
    </ClientProviders>
  );
}

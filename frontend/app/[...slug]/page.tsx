import { ClientApp } from '../client-app';
import { ClientProviders } from '../providers';

export default function CatchAllPage() {
  return <ClientProviders><ClientApp /></ClientProviders>;
}

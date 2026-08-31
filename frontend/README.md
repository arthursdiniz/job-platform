# Vértice — frontend da Job Platform

Interface web responsiva para a Job Platform API. O frontend consome exclusivamente os endpoints reais do backend Spring Boot; não há mocks permanentes nem dados de demonstração fixos nas telas de vagas e dashboards.

## Stack

- React 19 + TypeScript + Vinext/Vite
- React Router
- Axios + TanStack Query
- React Hook Form + Zod
- Tailwind CSS + shadcn/ui + Magic UI
- Motion, Lucide e Recharts
- Vitest + Testing Library

## Executar localmente

Com a API disponível em `http://localhost:8080`:

```bash
npm install
npm run dev
```

Abra `http://localhost:3000`.

Para apontar para outra API, crie um arquivo `.env.local`:

```env
VITE_API_URL=https://api.seudominio.com/api
NEXT_PUBLIC_SITE_URL=https://app.seudominio.com
```

O arquivo de ambiente é ignorado pelo Git. Em produção, configure as variáveis diretamente na plataforma de deploy.

## Credenciais locais

Quando a API roda com o perfil `dev`:

| Perfil | E-mail | Senha |
|---|---|---|
| Candidato | `candidate@example.com` | `Portfolio123!` |
| Empresa | `company@example.com` | `Portfolio123!` |

## Rotas principais

- Públicas: `/`, `/jobs`, `/jobs/:id`, `/login`, `/register`
- Candidato: `/candidate/dashboard`, `/candidate/recommended`, `/candidate/applications`, `/candidate/favorites`, `/candidate/profile`
- Empresa: `/company/dashboard`, `/company/jobs`, `/company/jobs/new`, `/company/jobs/:id/edit`, `/company/jobs/:id/applications`, `/company/profile`

Rotas protegidas validam o papel presente na sessão JWT. Como a API atual entrega o token no corpo e não oferece cookie HttpOnly, ele é guardado em `sessionStorage`, que é apagado ao fechar a aba. Para um ambiente de produção de maior risco, o próximo passo recomendado é adicionar refresh token rotativo em cookie HttpOnly no backend.

## Qualidade

```bash
npm run lint
npm test
npm run build
```

As capturas verificadas ficam em [`docs/screenshots`](./docs/screenshots).

## Deploy

O build gera a aplicação em `dist/`:

```bash
npm run build
npm start
```

Antes de publicar, faça também o deploy da API e do PostgreSQL, configure `VITE_API_URL` com a URL HTTPS pública e inclua o domínio do frontend em `ALLOWED_ORIGINS` no backend. A imagem social já está em `public/og.png`; defina `NEXT_PUBLIC_SITE_URL` para produzir URLs absolutas corretas nos metadados.

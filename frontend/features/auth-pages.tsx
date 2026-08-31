'use client';

import { Link, Navigate, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { ArrowRight, Building2, CheckCircle2, Loader2, UserRound } from 'lucide-react';
import { useState } from 'react';
import { useAuth } from '@/lib/auth';
import { apiError } from '@/lib/api';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';

const loginSchema = z.object({ email: z.email('Informe um e-mail válido.'), password: z.string().min(8, 'A senha deve ter pelo menos 8 caracteres.') });
const candidateSchema = loginSchema.extend({ name: z.string().min(2, 'Informe seu nome.'), headline: z.string().max(160).optional() });
const companySchema = loginSchema.extend({ ownerName: z.string().min(2, 'Informe seu nome.'), companyName: z.string().min(2, 'Informe o nome da empresa.') });

function Field({ label, error, ...props }: React.ComponentProps<typeof Input> & { label: string; error?: string }) { return <div className="grid gap-2"><Label htmlFor={props.id}>{label}</Label><Input {...props} aria-invalid={Boolean(error)} />{error && <p className="text-xs text-destructive">{error}</p>}</div>; }
function AuthFrame({ children, title, description }: { children: React.ReactNode; title: string; description: string }) { return <div className="grid min-h-[calc(100vh-4rem)] lg:grid-cols-[.9fr_1.1fr]"><div className="hidden bg-[#082f3a] p-12 text-white lg:flex lg:flex-col lg:justify-between"><Link to="/" className="text-xl font-semibold">Vértice</Link><div><p className="text-sm font-medium text-amber-300">Carreira com direção</p><blockquote className="mt-4 max-w-lg text-4xl font-semibold leading-tight tracking-tight">“O melhor match acontece quando expectativa e capacidade ficam claras para os dois lados.”</blockquote><div className="mt-8 grid gap-3 text-sm text-white/70">{['Recomendações por competências', 'Processos acompanhados em tempo real', 'Experiência segura e transparente'].map(item => <span key={item} className="flex items-center gap-2"><CheckCircle2 className="size-4 text-amber-300" />{item}</span>)}</div></div><p className="text-xs text-white/45">Plataforma de vagas para tecnologia</p></div><div className="flex items-center justify-center px-4 py-10"><Card className="w-full max-w-md border-0 shadow-none sm:border sm:shadow-xl"><CardHeader><CardTitle className="text-3xl">{title}</CardTitle><CardDescription>{description}</CardDescription></CardHeader><CardContent>{children}</CardContent></Card></div></div>; }

export function LoginPage() {
  const { user, login } = useAuth(); const navigate = useNavigate(); const [message, setMessage] = useState('');
  const form = useForm<z.infer<typeof loginSchema>>({ resolver: zodResolver(loginSchema), defaultValues: { email: '', password: '' } });
  if (user) return <Navigate to={user.role === 'COMPANY' ? '/company/dashboard' : '/candidate/dashboard'} replace />;
  const submit = form.handleSubmit(async data => { setMessage(''); try { const session = await login(data.email, data.password); navigate(session.user.role === 'COMPANY' ? '/company/dashboard' : '/candidate/dashboard'); } catch (e) { setMessage(apiError(e)); } });
  return <AuthFrame title="Que bom ter você de volta" description="Entre para continuar sua jornada na Vértice."><form onSubmit={submit} className="grid gap-5">{message && <Alert variant="destructive"><AlertDescription>{message}</AlertDescription></Alert>}<Field id="email" label="E-mail" type="email" autoComplete="email" error={form.formState.errors.email?.message} {...form.register('email')} /><Field id="password" label="Senha" type="password" autoComplete="current-password" error={form.formState.errors.password?.message} {...form.register('password')} /><Button type="submit" size="lg" disabled={form.formState.isSubmitting}>{form.formState.isSubmitting ? <Loader2 className="animate-spin" /> : null} Entrar <ArrowRight /></Button><p className="text-center text-sm text-muted-foreground">Ainda não tem conta? <Link className="font-medium text-primary hover:underline" to="/register">Criar conta</Link></p><div className="rounded-lg bg-secondary/60 p-3 text-xs leading-5 text-muted-foreground">Demonstração: <strong>candidate@example.com</strong> ou <strong>company@example.com</strong>, senha <strong>Portfolio123!</strong>.</div></form></AuthFrame>;
}

export function RegisterPage() {
  const { user, registerCandidate, registerCompany } = useAuth(); const navigate = useNavigate(); const [message, setMessage] = useState('');
  const candidate = useForm<z.infer<typeof candidateSchema>>({ resolver: zodResolver(candidateSchema), defaultValues: { name: '', email: '', password: '', headline: '' } });
  const company = useForm<z.infer<typeof companySchema>>({ resolver: zodResolver(companySchema), defaultValues: { ownerName: '', companyName: '', email: '', password: '' } });
  if (user) return <Navigate to={user.role === 'COMPANY' ? '/company/dashboard' : '/candidate/dashboard'} replace />;
  const candidateSubmit = candidate.handleSubmit(async data => { setMessage(''); try { await registerCandidate({ ...data, experienceLevel: 'JUNIOR' }); navigate('/candidate/profile'); } catch (e) { setMessage(apiError(e)); } });
  const companySubmit = company.handleSubmit(async data => { setMessage(''); try { await registerCompany(data); navigate('/company/profile'); } catch (e) { setMessage(apiError(e)); } });
  return <AuthFrame title="Crie seu espaço" description="Escolha como você quer participar da plataforma.">{message && <Alert variant="destructive" className="mb-4"><AlertDescription>{message}</AlertDescription></Alert>}<Tabs defaultValue="candidate"><TabsList className="mb-5 grid w-full grid-cols-2"><TabsTrigger value="candidate"><UserRound /> Candidato</TabsTrigger><TabsTrigger value="company"><Building2 /> Empresa</TabsTrigger></TabsList><TabsContent value="candidate"><form onSubmit={candidateSubmit} className="grid gap-4"><Field id="name" label="Nome completo" error={candidate.formState.errors.name?.message} {...candidate.register('name')} /><Field id="headline" label="Título profissional" placeholder="Ex.: Desenvolvedor Java Júnior" error={candidate.formState.errors.headline?.message} {...candidate.register('headline')} /><Field id="candidate-email" label="E-mail" type="email" error={candidate.formState.errors.email?.message} {...candidate.register('email')} /><Field id="candidate-password" label="Senha" type="password" error={candidate.formState.errors.password?.message} {...candidate.register('password')} /><Button type="submit" size="lg" disabled={candidate.formState.isSubmitting}>Criar perfil de candidato <ArrowRight /></Button></form></TabsContent><TabsContent value="company"><form onSubmit={companySubmit} className="grid gap-4"><Field id="ownerName" label="Seu nome" error={company.formState.errors.ownerName?.message} {...company.register('ownerName')} /><Field id="companyName" label="Nome da empresa" error={company.formState.errors.companyName?.message} {...company.register('companyName')} /><Field id="company-email" label="E-mail corporativo" type="email" error={company.formState.errors.email?.message} {...company.register('email')} /><Field id="company-password" label="Senha" type="password" error={company.formState.errors.password?.message} {...company.register('password')} /><Button type="submit" size="lg" disabled={company.formState.isSubmitting}>Criar conta da empresa <ArrowRight /></Button></form></TabsContent></Tabs><p className="mt-5 text-center text-sm text-muted-foreground">Já tem uma conta? <Link className="font-medium text-primary hover:underline" to="/login">Entrar</Link></p></AuthFrame>;
}

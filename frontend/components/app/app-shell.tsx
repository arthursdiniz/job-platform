'use client';

import { Link, NavLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { BriefcaseBusiness, Building2, ChevronDown, Compass, Heart, LayoutDashboard, LogOut, Menu, Moon, Search, Sun, UserRound } from 'lucide-react';
import { useState } from 'react';
import { useAuth } from '@/lib/auth';
import { initials } from '@/lib/format';
import { Button } from '@/components/ui/button';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { DropdownMenu, DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import { Sheet, SheetClose, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from '@/components/ui/sheet';
import { cn } from '@/lib/utils';

const publicLinks = [{ to: '/jobs', label: 'Explorar vagas', icon: Search }];
const candidateLinks = [
  { to: '/candidate/dashboard', label: 'Visão geral', icon: LayoutDashboard },
  { to: '/candidate/recommended', label: 'Recomendadas', icon: Compass },
  { to: '/candidate/applications', label: 'Candidaturas', icon: BriefcaseBusiness },
  { to: '/candidate/favorites', label: 'Favoritas', icon: Heart },
  { to: '/candidate/profile', label: 'Meu perfil', icon: UserRound },
];
const companyLinks = [
  { to: '/company/dashboard', label: 'Visão geral', icon: LayoutDashboard },
  { to: '/company/jobs', label: 'Minhas vagas', icon: BriefcaseBusiness },
  { to: '/company/profile', label: 'Empresa', icon: Building2 },
];

function Logo() {
  return <Link to="/" className="flex items-center gap-2 font-semibold tracking-tight" aria-label="Vértice — início"><span className="grid size-8 place-items-center rounded-md bg-primary text-sm font-bold text-primary-foreground">V</span><span className="text-lg">Vértice</span></Link>;
}

function ThemeButton() {
  const [dark, setDark] = useState(() => typeof document !== 'undefined' && document.documentElement.classList.contains('dark'));
  const toggle = () => { const next = !dark; setDark(next); document.documentElement.classList.toggle('dark', next); localStorage.setItem('vertice.theme', next ? 'dark' : 'light'); };
  return <Button variant="ghost" size="icon" onClick={toggle} aria-label={dark ? 'Usar tema claro' : 'Usar tema escuro'}>{dark ? <Sun /> : <Moon />}</Button>;
}

function AccountMenu() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  if (!user) return <div className="flex items-center gap-2"><Button variant="ghost" nativeButton={false} render={<Link to="/login" />}>Entrar</Button><Button nativeButton={false} render={<Link to="/register" />}>Criar conta</Button></div>;
  const home = user.role === 'COMPANY' ? '/company/dashboard' : user.role === 'CANDIDATE' ? '/candidate/dashboard' : '/jobs';
  return <DropdownMenu><DropdownMenuTrigger render={<Button variant="ghost" className="gap-2 px-2" />}><Avatar size="sm"><AvatarFallback className="bg-primary/10 text-primary">{initials(user.name)}</AvatarFallback></Avatar><span className="hidden max-w-36 truncate sm:inline">{user.name}</span><ChevronDown className="size-3.5" /></DropdownMenuTrigger><DropdownMenuContent align="end" className="w-56"><DropdownMenuGroup><DropdownMenuLabel><span className="block truncate">{user.name}</span><span className="block truncate text-xs font-normal text-muted-foreground">{user.email}</span></DropdownMenuLabel></DropdownMenuGroup><DropdownMenuSeparator /><DropdownMenuGroup><DropdownMenuItem onClick={() => void navigate(home)}>Minha área</DropdownMenuItem><DropdownMenuItem onClick={() => { logout(); void navigate('/'); }}><LogOut /> Sair</DropdownMenuItem></DropdownMenuGroup></DropdownMenuContent></DropdownMenu>;
}

function MobileNav({ links }: { links: typeof publicLinks }) {
  return <Sheet><SheetTrigger render={<Button variant="ghost" size="icon" className="lg:hidden" />}><Menu /></SheetTrigger><SheetContent side="left" className="w-[86%]"><SheetHeader><SheetTitle><Logo /></SheetTitle></SheetHeader><nav className="grid gap-1 px-3">{links.map(({ to, label, icon: Icon }) => <SheetClose key={to} render={<NavLink to={to} className={({ isActive }) => cn('flex items-center gap-3 rounded-lg px-3 py-3 text-sm', isActive ? 'bg-primary/10 font-medium text-primary' : 'text-muted-foreground')} />}><Icon className="size-4" />{label}</SheetClose>)}</nav></SheetContent></Sheet>;
}

export function AppShell() {
  const { user } = useAuth();
  const location = useLocation();
  const appArea = location.pathname.startsWith('/candidate') || location.pathname.startsWith('/company');
  const links = user?.role === 'CANDIDATE' ? candidateLinks : user?.role === 'COMPANY' ? companyLinks : publicLinks;
  return <div className="min-h-screen bg-background">
    <header className="sticky top-0 z-40 border-b bg-background/92 backdrop-blur-xl"><div className="mx-auto flex h-16 max-w-[1440px] items-center gap-4 px-4 sm:px-6"><MobileNav links={links} /><Logo /><nav className="ml-8 hidden items-center gap-1 lg:flex">{appArea ? links.map(({ to, label }) => <NavLink key={to} to={to} className={({ isActive }) => cn('rounded-md px-3 py-2 text-sm transition-colors', isActive ? 'bg-primary/10 font-medium text-primary' : 'text-muted-foreground hover:text-foreground')}>{label}</NavLink>) : <><NavLink to="/jobs" className="rounded-md px-3 py-2 text-sm text-muted-foreground hover:text-foreground">Explorar vagas</NavLink><Link to="/#como-funciona" className="rounded-md px-3 py-2 text-sm text-muted-foreground hover:text-foreground">Como funciona</Link></>}</nav><div className="ml-auto flex items-center gap-1"><ThemeButton /><AccountMenu /></div></div></header>
    <main><Outlet /></main>
    {!appArea && <footer className="border-t bg-card"><div className="mx-auto grid max-w-7xl gap-8 px-6 py-10 md:grid-cols-[1.5fr_1fr_1fr]"><div><Logo /><p className="mt-3 max-w-sm text-sm leading-6 text-muted-foreground">Conectando talentos e empresas por afinidade real de competências.</p></div><div><p className="text-sm font-medium">Produto</p><div className="mt-3 grid gap-2 text-sm text-muted-foreground"><Link to="/jobs">Vagas</Link><Link to="/register">Criar conta</Link></div></div><div><p className="text-sm font-medium">Plataforma</p><div className="mt-3 grid gap-2 text-sm text-muted-foreground"><a href="http://localhost:8080/swagger-ui/index.html" target="_blank" rel="noreferrer">Documentação da API</a><span>Feito para portfólio</span></div></div></div></footer>}
  </div>;
}

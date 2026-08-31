import axios from 'axios';

export const TOKEN_KEY = 'vertice.session';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api',
  timeout: 12_000,
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  if (typeof window !== 'undefined') {
    const raw = sessionStorage.getItem(TOKEN_KEY);
    if (raw) {
      const token = JSON.parse(raw)?.accessToken as string | undefined;
      if (token) config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && typeof window !== 'undefined') {
      sessionStorage.removeItem(TOKEN_KEY);
      window.dispatchEvent(new Event('vertice:unauthorized'));
    }
    return Promise.reject(error);
  },
);

export function apiError(error: unknown) {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data as { message?: string; detail?: string } | undefined;
    return data?.message ?? data?.detail ?? (error.code === 'ECONNABORTED' ? 'A API demorou para responder.' : 'Não foi possível concluir a operação.');
  }
  return 'Ocorreu um erro inesperado.';
}

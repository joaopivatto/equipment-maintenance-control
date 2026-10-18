/**
 * Prefixo das rotas do backend.
 *
 * - Produção: o nginx recebe `/api/...`, remove o prefixo e repassa ao Spring.
 * - Desenvolvimento (`ng serve`): o `proxy.conf.json` faz o mesmo para `http://localhost:8080`.
 *
 */
export const API_BASE_URL = '/api';

export type StatusTurma = 'ABERTA' | 'FECHADA';

export interface Turma {
  id: string;
  codigo: string;
  disciplinaId: string;
  periodoLetivoId: string;
  capacidadeMaxima: number;
  vagasDisponiveis: number;
  status: StatusTurma;
}

export interface CadastrarTurmaRequest {
  codigo: string;
  disciplinaId: string;
  periodoLetivoId: string;
  capacidadeMaxima: number;
  status: StatusTurma;
}

export interface AtualizarTurmaRequest {
  codigo: string;
  disciplinaId: string;
  periodoLetivoId: string;
  capacidadeMaxima: number;
}

export interface TurmaFiltros {
  codigo?: string;
  status?: StatusTurma | '';
  disciplinaId?: string;
  periodoLetivoId?: string;
  comVagas?: boolean | '';
}

export interface TurmasDisponiveisFiltros {
  disciplinaId?: string;
  periodoLetivoId?: string;
}

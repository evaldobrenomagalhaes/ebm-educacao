export type StatusMatricula = 'PENDENTE' | 'CONFIRMADA' | 'CANCELADA';

export interface Matricula {
  id: string;
  alunoId: string;
  turmaId: string;
  status: StatusMatricula;
}

export interface RealizarMatriculaRequest {
  alunoId: string;
  turmaId: string;
}

export interface MatriculaFiltros {
  status?: StatusMatricula | '';
  alunoId?: string;
  turmaId?: string;
  periodoLetivoId?: string;
  disciplinaId?: string;
}

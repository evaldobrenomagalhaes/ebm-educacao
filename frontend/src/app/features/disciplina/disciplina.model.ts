export interface Disciplina {
  id: string;
  nome: string;
  codigo: string;
  cursoId: string;
}

export interface DisciplinaRequest {
  nome: string;
  codigo: string;
  cursoId: string;
}

export interface DisciplinaFiltros {
  nome?: string;
  codigo?: string;
  cursoId?: string;
}

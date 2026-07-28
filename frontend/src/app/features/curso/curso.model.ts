export type SituacaoCurso = 'ATIVO' | 'INATIVO';

export interface Curso {
  id: string;
  nome: string;
  situacao: SituacaoCurso;
}

export interface CursoRequest {
  nome: string;
  situacao: SituacaoCurso;
}

export interface CursoFiltros {
  nome?: string;
  situacao?: SituacaoCurso | '';
}

export type SituacaoAcademica = 'ATIVO' | 'INATIVO';

export interface Aluno {
  id: string;
  nome: string;
  email: string;
  situacaoAcademica: SituacaoAcademica;
}

export interface AlunoRequest {
  nome: string;
  email: string;
  situacaoAcademica: SituacaoAcademica;
}

export interface AlunoFiltros {
  nome?: string;
  email?: string;
  situacaoAcademica?: SituacaoAcademica | '';
}

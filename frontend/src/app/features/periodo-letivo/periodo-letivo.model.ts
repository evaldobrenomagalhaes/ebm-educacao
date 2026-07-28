export type SituacaoPeriodoLetivo = 'ABERTO' | 'ENCERRADO';

export interface PeriodoLetivo {
  id: string;
  codigo: string;
  dataInicio: string;
  dataTermino: string;
  situacao: SituacaoPeriodoLetivo;
}

export interface PeriodoLetivoRequest {
  codigo: string;
  dataInicio: string;
  dataTermino: string;
  situacao: SituacaoPeriodoLetivo;
}

export interface PeriodoLetivoFiltros {
  codigo?: string;
  situacao?: SituacaoPeriodoLetivo | '';
  dataInicioDe?: string;
  dataInicioAte?: string;
  dataTerminoDe?: string;
  dataTerminoAte?: string;
  vigenteEm?: string;
}

import { StatusMatricula } from '../../features/matricula/matricula.model';
import { SituacaoAcademica } from '../../features/aluno/aluno.model';
import { SituacaoCurso } from '../../features/curso/curso.model';
import { SituacaoPeriodoLetivo } from '../../features/periodo-letivo/periodo-letivo.model';
import { StatusTurma } from '../../features/turma/turma.model';

const situacaoCurso: Record<SituacaoCurso, string> = {
  ATIVO: 'Ativo',
  INATIVO: 'Inativo',
};

const situacaoAcademica: Record<SituacaoAcademica, string> = {
  ATIVO: 'Ativo',
  INATIVO: 'Inativo',
};

const situacaoPeriodo: Record<SituacaoPeriodoLetivo, string> = {
  ABERTO: 'Aberto',
  ENCERRADO: 'Encerrado',
};

const statusTurma: Record<StatusTurma, string> = {
  ABERTA: 'Aberta',
  FECHADA: 'Fechada',
};

const statusMatricula: Record<StatusMatricula, string> = {
  PENDENTE: 'Pendente',
  CONFIRMADA: 'Confirmada',
  CANCELADA: 'Cancelada',
};

export const Labels = {
  situacaoCurso: (v: SituacaoCurso) => situacaoCurso[v] ?? v,
  situacaoAcademica: (v: SituacaoAcademica) => situacaoAcademica[v] ?? v,
  situacaoPeriodo: (v: SituacaoPeriodoLetivo) => situacaoPeriodo[v] ?? v,
  statusTurma: (v: StatusTurma) => statusTurma[v] ?? v,
  statusMatricula: (v: StatusMatricula) => statusMatricula[v] ?? v,
};

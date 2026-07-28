package br.com.academico.infrastructure.event;

import br.com.academico.domain.event.MatriculaCancelada;
import br.com.academico.domain.event.MatriculaConfirmada;
import br.com.academico.domain.event.MatriculaRealizada;
import br.com.academico.domain.event.TurmaAberta;
import br.com.academico.domain.event.TurmaFechada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Consumidor MVP dos eventos de domínio — auditoria via log após commit (doc 08 / MD-011).
 */
@Component
public class DomainEventLoggingListener {

    private static final Logger log = LoggerFactory.getLogger(DomainEventLoggingListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMatriculaRealizada(MatriculaRealizada event) {
        log.info(
                "MatriculaRealizada matriculaId={} alunoId={} turmaId={}",
                event.matriculaId().valor(),
                event.alunoId().valor(),
                event.turmaId().valor()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMatriculaConfirmada(MatriculaConfirmada event) {
        log.info(
                "MatriculaConfirmada matriculaId={} alunoId={} turmaId={}",
                event.matriculaId().valor(),
                event.alunoId().valor(),
                event.turmaId().valor()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMatriculaCancelada(MatriculaCancelada event) {
        log.info(
                "MatriculaCancelada matriculaId={} alunoId={} turmaId={}",
                event.matriculaId().valor(),
                event.alunoId().valor(),
                event.turmaId().valor()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTurmaAberta(TurmaAberta event) {
        log.info("TurmaAberta turmaId={}", event.turmaId().valor());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTurmaFechada(TurmaFechada event) {
        log.info("TurmaFechada turmaId={}", event.turmaId().valor());
    }
}

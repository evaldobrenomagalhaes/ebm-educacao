import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { environment } from '../../../environments/environment';
import { MatriculaForm } from './matricula-form';

describe('MatriculaForm', () => {
  let fixture: ComponentFixture<MatriculaForm>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatriculaForm, NoopAnimationsModule, MatSnackBarModule],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(MatriculaForm);
    fixture.detectChanges();

    const alunoReq = httpMock.expectOne(`${environment.apiUrl}/api/alunos`);
    alunoReq.flush([{ id: 'a1', nome: 'Ana', email: 'ana@ex.com', situacaoAcademica: 'ATIVO' }]);

    const turmaReq = httpMock.expectOne(`${environment.apiUrl}/api/turmas/disponiveis`);
    turmaReq.flush([
      {
        id: 't1',
        codigo: 'T01',
        disciplinaId: 'd1',
        periodoLetivoId: 'p1',
        capacidadeMaxima: 30,
        vagasDisponiveis: 5,
        status: 'ABERTA',
      },
    ]);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('exibe o título de nova matrícula', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Nova matrícula');
  });

  it('submete aluno e turma selecionados', () => {
    const component = fixture.componentInstance;
    component.form.setValue({ alunoId: 'a1', turmaId: 't1' });
    component.salvar();

    const req = httpMock.expectOne(`${environment.apiUrl}/api/matriculas`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ alunoId: 'a1', turmaId: 't1' });
    req.flush({ id: 'm1', alunoId: 'a1', turmaId: 't1', status: 'PENDENTE' });
  });
});

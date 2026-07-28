import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';
import { MatriculaService } from './matricula.service';

describe('MatriculaService', () => {
  let service: MatriculaService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(MatriculaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('lista matrículas filtrando por status', () => {
    const mock = [
      { id: 'm1', alunoId: 'a1', turmaId: 't1', status: 'PENDENTE' as const },
    ];

    service.listar({ status: 'PENDENTE' }).subscribe((data) => {
      expect(data).toEqual(mock);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/api/matriculas?status=PENDENTE`);
    expect(req.request.method).toBe('GET');
    req.flush(mock);
  });

  it('realiza matrícula', () => {
    const body = { alunoId: 'a1', turmaId: 't1' };
    const response = { id: 'm1', ...body, status: 'PENDENTE' as const };

    service.realizar(body).subscribe((data) => {
      expect(data.status).toBe('PENDENTE');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/api/matriculas`);
    expect(req.request.method).toBe('POST');
    req.flush(response);
  });

  it('confirma matrícula', () => {
    const response = {
      id: 'm1',
      alunoId: 'a1',
      turmaId: 't1',
      status: 'CONFIRMADA' as const,
    };

    service.confirmar('m1').subscribe((data) => {
      expect(data.status).toBe('CONFIRMADA');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/api/matriculas/m1/confirmar`);
    expect(req.request.method).toBe('POST');
    req.flush(response);
  });
});

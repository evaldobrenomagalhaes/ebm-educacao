import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';
import { CursoService } from './curso.service';

describe('CursoService', () => {
  let service: CursoService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(CursoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('lista cursos com filtros opcionais', () => {
    const mock = [{ id: '1', nome: 'ADS', situacao: 'ATIVO' as const }];

    service.listar({ nome: 'ADS', situacao: 'ATIVO' }).subscribe((data) => {
      expect(data).toEqual(mock);
    });

    const req = httpMock.expectOne(
      `${environment.apiUrl}/api/cursos?nome=ADS&situacao=ATIVO`,
    );
    expect(req.request.method).toBe('GET');
    req.flush(mock);
  });

  it('cadastra curso', () => {
    const body = { nome: 'SI', situacao: 'ATIVO' as const };
    const response = { id: '2', ...body };

    service.cadastrar(body).subscribe((data) => {
      expect(data).toEqual(response);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/api/cursos`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(body);
    req.flush(response);
  });
});

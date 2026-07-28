import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { ApiError } from '../api/api-error';
import { errorInterceptor } from './error.interceptor';

describe('errorInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatSnackBarModule],
      providers: [provideHttpClient(withInterceptors([errorInterceptor])), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('normaliza ProblemDetail 409 em ApiError', () => {
    let captured: unknown;
    http.get('/api/test').subscribe({
      error: (err) => {
        captured = err;
      },
    });

    const req = httpMock.expectOne('/api/test');
    req.flush(
      { title: 'Conflito', status: 409, detail: 'Não há vagas disponíveis.' },
      { status: 409, statusText: 'Conflict' },
    );

    expect(captured).toBeInstanceOf(ApiError);
    const apiError = captured as ApiError;
    expect(apiError.status).toBe(409);
    expect(apiError.detail).toBe('Não há vagas disponíveis.');
  });

  it('propaga erros de validação 400', () => {
    let captured: unknown;
    http.post('/api/test', {}).subscribe({
      error: (err) => {
        captured = err;
      },
    });

    const req = httpMock.expectOne('/api/test');
    req.flush(
      {
        title: 'Requisição inválida',
        status: 400,
        detail: 'Um ou mais campos são inválidos',
        errors: { nome: 'must not be blank' },
      },
      { status: 400, statusText: 'Bad Request' },
    );

    expect(captured).toBeInstanceOf(ApiError);
    expect((captured as ApiError).errors?.['nome']).toBe('must not be blank');
  });
});

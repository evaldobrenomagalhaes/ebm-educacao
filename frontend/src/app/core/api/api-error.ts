export interface ProblemDetail {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  errors?: Record<string, string>;
}

export class ApiError extends Error {
  readonly status: number;
  readonly title: string;
  readonly detail: string;
  readonly errors?: Record<string, string>;
  readonly problem: ProblemDetail;

  constructor(problem: ProblemDetail) {
    super(problem.detail || problem.title || 'Erro na API');
    this.name = 'ApiError';
    this.problem = problem;
    this.status = problem.status ?? 0;
    this.title = problem.title ?? 'Erro';
    this.detail = problem.detail ?? this.message;
    this.errors = problem.errors;
  }

  get isNotFound(): boolean {
    return this.status === 404;
  }

  get isValidation(): boolean {
    return this.status === 400 && !!this.errors;
  }

  get isConflictOrBusiness(): boolean {
    return this.status === 409 || this.status === 422;
  }
}

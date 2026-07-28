import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

import { ApiError, ProblemDetail } from '../api/api-error';
import { FeedbackService } from '../services/feedback.service';

function toProblemDetail(error: HttpErrorResponse): ProblemDetail {
  if (error.status === 0) {
    return {
      status: 0,
      title: 'Falha de comunicação',
      detail: 'Falha de comunicação. Tente novamente.',
    };
  }

  const body = error.error;
  if (body && typeof body === 'object') {
    return {
      type: body.type,
      title: body.title ?? error.statusText,
      status: body.status ?? error.status,
      detail: body.detail,
      instance: body.instance,
      errors: body.errors,
    };
  }

  if (error.status >= 500) {
    return {
      status: error.status,
      title: 'Erro interno',
      detail: 'Falha de comunicação. Tente novamente.',
    };
  }

  return {
    status: error.status,
    title: error.statusText || 'Erro',
    detail: typeof body === 'string' && body ? body : 'Não foi possível concluir a operação.',
  };
}

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const feedback = inject(FeedbackService);

  return next(req).pipe(
    catchError((error: unknown) => {
      if (!(error instanceof HttpErrorResponse)) {
        return throwError(() => error);
      }

      const problem = toProblemDetail(error);
      const apiError = new ApiError(problem);

      if (apiError.status === 0 || apiError.status >= 500) {
        feedback.error(apiError.detail);
      } else if (apiError.isConflictOrBusiness) {
        feedback.error(apiError.detail);
      } else if (apiError.status === 400 && !apiError.errors) {
        feedback.error(apiError.detail);
      } else if (apiError.status === 404) {
        // Feature pages handle 404 empty state; snackbar only for non-GET when useful.
        if (req.method !== 'GET') {
          feedback.error(apiError.detail);
        }
      }

      return throwError(() => apiError);
    }),
  );
};

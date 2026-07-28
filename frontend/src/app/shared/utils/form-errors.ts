import { ApiError } from '../../core/api/api-error';

export function applyFieldErrors(
  error: unknown,
  setErrors: (field: string, message: string) => void,
): void {
  if (error instanceof ApiError && error.errors) {
    for (const [field, message] of Object.entries(error.errors)) {
      setErrors(field, message);
    }
  }
}

export function isNotFound(error: unknown): boolean {
  return error instanceof ApiError && error.isNotFound;
}

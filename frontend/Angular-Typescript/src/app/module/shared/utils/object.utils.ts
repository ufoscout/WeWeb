/**
 * Returns true if the object is not undefined and not null.
 */
export function exists(obj: any | null | undefined): boolean {
  return (!(obj === undefined) && !(obj === null));
}

/**
 * Executes the callback if the object exists.
 */
export function ifExists<T>(obj: T | null | undefined, callback: (obj: T) => void) {
  if (exists(obj)) {
    callback(obj as T);
  }
}

/**
 * Returns value if exists, otherwise defaultValue.
 */
export function getOrDefault<T>(value: T | null | undefined, defaultValue: T): T {
  return exists(value) ? (value as T) : defaultValue;
}

/**
 * Executes the callback if the object exists, otherwise return the defaultValue.
 */
export function ifExistsOrDefault<T, R>(obj: T | null | undefined, defaultValue: R, callback: (obj: T) => R): R {
  if (exists(obj)) {
    return callback(obj as T);
  }
  return defaultValue;
}

/**
 * Returns a deep clone of the source object
 */
export function deepClone<T>(source: T): T {
  if (source) {
      return JSON.parse(JSON.stringify(source));
  }
  return source;
}

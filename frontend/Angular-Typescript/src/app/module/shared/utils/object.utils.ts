/**
 * Returns true if the object is not undefined and not null.
 */
export function exists(obj: any | null | undefined): boolean {
  return (!(obj === undefined) && !(obj === null));
}

/**
 * Returns value if exists, otherwise defaultValue.
 */
export function getOrDefault<T>(value: T | null | undefined, defaultValue: T): T {
  return exists(value) ? (value as T) : defaultValue;
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

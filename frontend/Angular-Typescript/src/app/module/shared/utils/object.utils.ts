/**
 * Returns true if the object is not undefined and not null.
 */
export function exists(obj: any): boolean {
  return (!(obj === undefined) && !(obj === null));
}

/**
 * Returns value if exists, otherwise defaultValue.
 */
export function getOrDefault<T>(value: T, defaultValue: T): T {
  return exists(value) ? value : defaultValue;
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

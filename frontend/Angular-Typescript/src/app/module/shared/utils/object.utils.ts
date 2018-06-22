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

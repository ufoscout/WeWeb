/**
 * Returns true if the object is not undefined and not null
 */
export function exists(obj: any): boolean {
  return (!isUndefined(obj) && !isNull(obj));
}

/**
 * Returns true if the object is undefined
 */
export function isUndefined(obj: any): boolean {
  return obj === undefined;
}

/**
 * Returns true if the object is null
 */
export function isNull(obj: any): boolean {
  return obj === null;
}

/**
 * Returns a deep copy of the source object
 */
export function deepClone<T>(source: T): T {
  if (exists(source)) {
      return JSON.parse(JSON.stringify(source));
  }
  return source;
}

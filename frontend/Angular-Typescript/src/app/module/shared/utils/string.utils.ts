import * as obj from './object.utils';

/**
 * Checks if a string is empty (""), undefined, null or whitespace only.
 */
export function isBlank(text: string | null | undefined): boolean {
  return !obj.exists(text) || (text as string).trim().length === 0;
}

/**
 * Checks if a string is empty (""), undefined or null.
 */
export function isEmpty(text: string | null | undefined): boolean {
  return !obj.exists(text) || (text as string).length === 0;
}

/**
 * Returns value if not empty, otherwise an empty string.
 */
export function getOrEmpty(value: string | null | undefined): string {
  return getOrDefault(value, '');
}

/**
 * Returns value if not empty, otherwise defaultValue.
 */
export function getOrDefault(value: string | null | undefined, defaultValue: string): string {
  return isEmpty(value) ? defaultValue : (value as string);
}

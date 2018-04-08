import * as obj from './object.utils';

/**
 * Checks if a string is empty (""), undefined, null or whitespace only.
 */
export function isBlank(text: string): boolean {
  return !obj.exists(text) || text.trim().length === 0;
}

/**
 * Checks if a string is empty (""), undefined or null.
 */
export function isEmpty(text: string): boolean {
  return !obj.exists(text) || text.length === 0;
}

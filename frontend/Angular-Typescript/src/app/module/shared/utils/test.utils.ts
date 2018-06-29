
/**
 * Return whether the code is running in a test
 */
export function inTests(): boolean {
  return window.hasOwnProperty('jasmine');
}

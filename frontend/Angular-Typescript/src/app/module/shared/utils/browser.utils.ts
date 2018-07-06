import * as obj from './object.utils';

/**
 * Returns the first language in the preferred languages list
 */
export function getLang(): string {
  return getLangs()[0];
}

/**
 * Returns the User preferred languages list
 */
export function getLangs(): ReadonlyArray<string> {
  if (obj.exists(navigator.languages)) {
    return navigator.languages;
  } else if (obj.exists(navigator.language)) {
    return [navigator.language];
  } else {
    // Backward compatibility for IE9 and IE10
    return [(navigator as any).browserLanguage];
  }
}

/**
 * Given an array of supported languages, it returns the first preferred language that is supported.
 * It returns the defaultLang if there is no match between supported and preferred languages.
 */
export function getPreferredSupportedLang(supportedLangs: string[], defaultLang: string): string {
  for (const lang of getLangs()) {
    if (supportedLangs.includes(lang)) {
      return lang;
    }
  }
  return defaultLang;
}

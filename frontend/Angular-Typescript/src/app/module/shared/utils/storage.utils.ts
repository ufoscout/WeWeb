export function filterProperties<T>(source: T, properties: string[]): any {
    return Object.keys(source)
        .filter(key => properties.includes(key))
        .reduce((obj, key) => {
            return {
                ...obj,
                [key]: source[key]
            };
        }, {});
}

interface StorageHelper {
    fromOrDefault<T>(key: string, defaultValue: T): T;
    store<T>(key: string, value: T): void;
    remove<T>(key: string): void;
    clear(): void;
}

export class LocalStorageHelper implements StorageHelper {

    fromOrDefault<T>(key: string, defaultValue: T): T {
        const value = localStorage.getItem(key);
        if (!value) {
            return defaultValue;
        }
        return JSON.parse(value);
    }

    store<T>(key: string, value: T): void {
        localStorage.setItem(key, JSON.stringify(value));
    }

    remove<T>(key: string): void {
        localStorage.removeItem(key);
    }

    clear(): void {
        localStorage.clear();
    }
}

export class CookieStorageHelper implements StorageHelper {

    protected static readonly COOKIE_MATCH: RegExp = /\w+=([^;]*)/;
    protected static readonly COOKIE_MATCH_ALL_KEYS: RegExp = /(\w+)=[^;]*/g;
    protected static readonly COOKIE_TTL: number = 1000 * 60 * 60 * 24 * 365 * 100; // ABOUT A HUNDRED YEARS

    protected static cookiePosition(key: string): any {
        let position = null;

        if (document.cookie.length && key) {
            const start = document.cookie.indexOf(key);
            if (start !== -1) {
                let end = document.cookie.substring(start).indexOf(';');

                if (end === -1) {
                    end = document.cookie.length - 1;
                }

                position = { start: start, end: end };
            }
        }

        return position;
    }

    protected static getNewCookieExpirationDate(): string {
        const expirationDate = new Date();

        expirationDate.setTime(expirationDate.getTime() + CookieStorageHelper.COOKIE_TTL);

        return expirationDate.toUTCString();
    }

    fromOrDefault<T>(key: string, defaultValue: T): T {
        const position = CookieStorageHelper.cookiePosition(key);
        let value = defaultValue;

        if (position) {
            const cookieString = document.cookie.substring(position.start, position.end + 1);
            const cookieMatch = cookieString.match(CookieStorageHelper.COOKIE_MATCH);

            if (cookieMatch) {
                try {
                    value = JSON.parse(cookieMatch[1]);
                } catch (error) {
                    console.error(error);
                }
            }
        }

        return value;
    }

    store<T>(key: string, value: T): void {
        document.cookie = `${key}=${JSON.stringify(value)}; expires=${CookieStorageHelper.getNewCookieExpirationDate()}; path=/;`;
    }

    remove<T>(key: string): void {
        const position = CookieStorageHelper.cookiePosition(key);

        if (position) {
            document.cookie = `${key}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
        }
    }

    clear(): void {
        if (document.cookie) {
            for (const cookie of document.cookie.match(CookieStorageHelper.COOKIE_MATCH_ALL_KEYS)) {
                const key = cookie.substring(0, cookie.indexOf('='));
                document.cookie = `${key}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
            }
        }
    }
}

export function getStorageHelper(): StorageHelper {
    let useLocalStorage = false;

    try {
        useLocalStorage = typeof window['localStorage'] !== 'undefined';
    } catch (DOMException) {
        // localStorage is not available
    }

    return useLocalStorage
        ? new LocalStorageHelper()
        : new CookieStorageHelper();
}

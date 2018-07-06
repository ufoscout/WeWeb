import * as broswer from './browser.utils';

describe('[Shared] Browser utils', () => {

    let languages: ReadonlyArray<string>;

    beforeEach(() => {
        languages = navigator.languages;
    });

    afterEach(() => {
        setLangs(languages);
    });

    function setLangs(langs: ReadonlyArray<string>) {
        window.navigator['__defineGetter__']('languages', function () {
            return langs;
        });
    }

    it('should contain at least one language', () => {
        expect(
            broswer.getLangs().length > 0
        )
            .toBeTruthy();
    });

    it('should not be undefined', () => {
        expect(
            broswer.getLang()
        )
            .toBeDefined();
    });

    it('should return the first preferred language', () => {
        setLangs(['it', 'en', 'fr']);
        expect(
            broswer.getLang()
        )
            .toBe('it');
    });

    it('should return the first matching supported preferred language', () => {
        setLangs(['it', 'en', 'fr']);
        expect(broswer.getPreferredSupportedLang(['fr'], 'en')).toBe('fr');
        expect(broswer.getPreferredSupportedLang(['it'], 'en')).toBe('it');
        expect(broswer.getPreferredSupportedLang(['it', 'fr', 'en'], 'en')).toBe('it');
        expect(broswer.getPreferredSupportedLang(['fr', 'en'], 'it')).toBe('en');
    });

    it('should return the default language if no match', () => {
        setLangs(['it', 'en', 'fr']);
        expect(broswer.getPreferredSupportedLang(['jp', 'de'], 'en')).toBe('en');
    });

});

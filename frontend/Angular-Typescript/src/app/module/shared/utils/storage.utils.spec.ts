import * as storage from './storage.utils';

const KEY_1 = 'key1';
const KEY_2 = 'key2';

const storages = [
    new storage.LocalStorageHelper(),
    new storage.CookieStorageHelper()
];

for (const nthStorage of storages) {
    describe('Storage helper ' + nthStorage.constructor.name, () => {

        beforeAll(() => {
            this.storage = nthStorage;
        });

        beforeEach(() => {
            this.storage.clear();
        });

        it('should persit a string', () => {

            const source = 'a string value';

            this.storage.store(KEY_1, source);

            expect(
                this.storage.fromOrDefault(KEY_1, '')
            )
                .toEqual(source);
        });

        it('should persit based on the key', () => {

            const source1 = 'a string value 1';
            const source2 = 'a string value 2';

            this.storage.store(KEY_1, source1);
            this.storage.store(KEY_2, source2);

            expect(
                this.storage.fromOrDefault(KEY_1, '')
            )
                .toEqual(source1);

            expect(
                this.storage.fromOrDefault(KEY_2, '')
            )
                .toEqual(source2);
        });

        it('should persit a number', () => {

            const source = 123456;

            this.storage.store(KEY_1, source);

            expect(
                this.storage.fromOrDefault(KEY_1, 0)
            )
                .toEqual(source);
        });

        it('should return the default value', () => {
            const defaultValue = 2134545;
            expect(
                this.storage.fromOrDefault(KEY_1, defaultValue)
            )
                .toEqual(defaultValue);
        });

        it('should persit an object', () => {

            const source = {
                age: 123456,
                name: 'Arthur Dent',
            };

            this.storage.store(KEY_1, source);

            expect(
                this.storage.fromOrDefault(KEY_1, {})
            )
                .toEqual(source);
        });

        it('should remove based on key', () => {

            const source = 'a string value';
            this.storage.store(KEY_1, source);
            this.storage.remove(KEY_1);

            expect(
                this.storage.fromOrDefault(KEY_1, '')
            )
                .toEqual('');
        });

        it('should clear the stored values when asked for', () => {
            this.storage.store(KEY_1, '');
            this.storage.store(KEY_2, '');

            this.storage.clear();

            expect(
                this.storage.fromOrDefault(KEY_1, '')
            )
                .toEqual('');

            expect(
                this.storage.fromOrDefault(KEY_2, '')
            )
                .toEqual('');
        });

    });
}

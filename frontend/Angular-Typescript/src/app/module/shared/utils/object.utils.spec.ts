import * as obj from './object.utils';

describe('[Shared] Object utils', () => {

    it('should not exist if undefined', () => {
        expect(
            obj.exists(undefined)
        )
            .toBeFalsy();
    });

    it('should not exist if null', () => {
        expect(
            obj.exists(null)
        )
            .toBeFalsy();
    });

    it('should exist if not undefined and not null', () => {
        expect(
            obj.exists('undefined')
        )
            .toBeTruthy();
    });

    it('should not execute if null', () => {
        let executed = false;

        obj.ifExists(null, val => {
            executed = true;
        });

        expect(executed).toBeFalsy();
    });

    it('should execute if not undefined and not null', () => {
        let executed = false;
        const expected = '1235';
        let found = '';

        obj.ifExists(expected, val => {
            executed = true;
            found = val;
        });

        expect(executed).toBeTruthy();
        expect(found).toBe(expected);
    });

    it('should return defaul if value is undefined', () => {
        expect(
            obj.getOrDefault(undefined, 'default')
        )
            .toBe('default');
    });

    it('should return defaul if value is null', () => {
        expect(
            obj.getOrDefault(null, 'default')
        )
            .toBe('default');
    });

    it('should return value if is not undefined and not null', () => {
        expect(
            obj.getOrDefault<string>('value', 'default')
        )
            .toBe('value');
    });

    it('should not execute and return the default if null', () => {
        let executed = false;
        const defaultVal = '1235';

        const found = obj.ifExistsOrDefault(null, defaultVal, val => {
            executed = true;
            return val;
        });

        expect(executed).toBeFalsy();
        expect(found).toBe(defaultVal);
    });

    it('should execute with value if not undefined', () => {
        let executed = false;
        const defaultVal = '5433';
        const expected = '1235';

        const found = obj.ifExistsOrDefault(expected, defaultVal, val => {
            executed = true;
            return val;
        });

        expect(executed).toBeTruthy();
        expect(found).toBe(expected);
    });

    it('should deep clone an object', () => {
        const source = {
            name: 'Michael Schumacher',
            age: 49,
            birthdate: new Date().getTime(),
            address: {
                city: 'Rome',
                country: 'Italy',
            }
        };

        const dest = obj.deepClone(source);

        expect(dest).not.toBe(source);
        expect(dest.name).toEqual(source.name);
        expect(dest.age).toEqual(source.age);
        expect(dest.birthdate).toEqual(source.birthdate);

        expect(dest.address).not.toBe(source.address);
        expect(dest.address.city).toEqual(source.address.city);
        expect(dest.address.country).toEqual(source.address.country);
    });

    it('deep clone should handle null gracefully', () => {
        expect(obj.deepClone(null)).toBeNull();
    });

    it('deep clone should handle undefined gracefully', () => {
        expect(obj.deepClone(undefined)).toBeUndefined();
    });

});

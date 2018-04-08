import * as obj from './object.utils';

describe('Object helper', () => {

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

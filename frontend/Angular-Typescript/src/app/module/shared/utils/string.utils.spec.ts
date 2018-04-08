import * as str from './string.utils';

describe('Object helper', () => {

    it('should be blank if undefined', () => {
        expect(
            str.isBlank(undefined)
        )
            .toBeTruthy();
    });

    it('should be blank if null', () => {
        expect(
            str.isBlank(null)
        )
            .toBeTruthy();
    });

    it('should be blank if lenght is 0', () => {
        expect(
            str.isBlank('')
        )
            .toBeTruthy();
    });

    it('should be blank if lenght > 0 but it contains only white spaces', () => {
        expect(
            str.isBlank(' ')
        )
            .toBeTruthy();
    });

    it('should be not blank if lenght > 0', () => {
        expect(
            str.isBlank(' undefined ')
        )
            .toBeFalsy();
    });

    it('should be empty if undefined', () => {
        expect(
            str.isEmpty(undefined)
        )
            .toBeTruthy();
    });

    it('should be empty if null', () => {
        expect(
            str.isEmpty(null)
        )
            .toBeTruthy();
    });

    it('should be empty if lenght is 0', () => {
        expect(
            str.isEmpty('')
        )
            .toBeTruthy();
    });

    it('should be not empty if lenght > 0 and it contains only white spaces', () => {
        expect(
            str.isEmpty(' ')
        )
            .toBeFalsy();
    });

    it('should be not empty if lenght > 0', () => {
        expect(
            str.isEmpty(' undefined ')
        )
            .toBeFalsy();
    });
});

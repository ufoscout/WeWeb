import * as t from './test.utils';

describe('[Shared] Test utils', () => {

    it('should return true during tests', () => {
        expect(
            t.inTests()
        )
            .toBeTruthy();
    });

});

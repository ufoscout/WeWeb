import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed } from '@angular/core/testing';
import { CreateUser } from './um.events';
import { UmModule } from '.';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CreateUserDto } from './generated/dto';
import { UmService } from './um.service';

describe('[Um] Um State', () => {
    let store: Store;
    let umService: UmService;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                NgxsModule.forRoot(),
                HttpClientTestingModule,
                UmModule,
            ],
        }).compileComponents();
        store = TestBed.get(Store);
        umService = TestBed.get(UmService);
    }));

    it('it should call the createUser service method', () => {
        const createUserSpy = spyOn(umService, 'createUser').and.callThrough();

        const dto: CreateUserDto = { email: '', password: '', passwordConfirm: '' };
        store.dispatch( new CreateUser(dto) );

        expect(createUserSpy).toHaveBeenCalledWith(dto);
    });

});

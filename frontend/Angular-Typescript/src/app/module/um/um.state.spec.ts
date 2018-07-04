import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed } from '@angular/core/testing';
import { UmModule } from '.';
import { HttpClientTestingModule } from '@angular/common/http/testing';
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

});

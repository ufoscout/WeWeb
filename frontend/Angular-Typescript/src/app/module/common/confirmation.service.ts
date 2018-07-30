import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmComponent } from './confirm/confirm.component';
import * as obj from '../shared/utils/object.utils';
import { TranslateService } from '@ngx-translate/core';

export interface OnConfirm {
    bodyText?: string;
    bodyKey?: string;
    titleText?: string;
    titleKey?: string;
    confirm: () => void;
    cancel?: () => void;
}

@Injectable()
export class ConfirmationService {

    constructor(private modalService: NgbModal, private translate: TranslateService) { }

    openConfirm(onConfirm: OnConfirm) {
        const ref = this.modalService.open(ConfirmComponent);

        ref.componentInstance.title = this.getText(onConfirm.titleKey, onConfirm.titleText);
        ref.componentInstance.body = this.getText(onConfirm.bodyKey, onConfirm.bodyText);

            ref.result
            .then(res => {
                if (res === 'confirm') {
                    onConfirm.confirm();
                } else {
                    obj.ifExists(onConfirm.cancel, (canc) => canc());
                }
            })
            .catch(_ => obj.ifExists(onConfirm.cancel, (canc) => canc()));
    }

    private getText(key?: string, text?: string): string {
        let result = '';
        obj.ifExists(text, (val) => result = val);
        obj.ifExists(key, (val) => result = this.translate.instant(val));
        return result;
    }
}

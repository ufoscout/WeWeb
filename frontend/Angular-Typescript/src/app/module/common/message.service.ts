import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class MessageService {

    constructor(private toastr: ToastrService, private translate: TranslateService) { }

    /** show successful toast */
    success(message?: string, title?: string) {
        this.toastr.success(message, title);
    }

    /** show successful toast */
    successByKey(messageKey: string) {
        this.translate.get(messageKey).subscribe((res: string) => {
            this.toastr.success(res);
        });
    }

    /** show error toast */
    error(message?: string, title?: string) {
        this.toastr.error(message, title);
    }

    /** show error toast */
    errorByKey(messageKey: string) {
        this.translate.get(messageKey).subscribe((res: string) => {
            this.toastr.error(res);
        });
    }

    /** show info toast */
    info(message?: string, title?: string) {
        this.toastr.info(message, title);
    }

    /** show info toast */
    infoByKey(messageKey: string) {
        this.translate.get(messageKey).subscribe((res: string) => {
            this.toastr.info(res);
        });
    }

    /** show warning toast */
    warning(message?: string, title?: string) {
        this.toastr.warning(message, title);
    }

    /** show warning toast */
    warningByKey(messageKey: string) {
        this.translate.get(messageKey).subscribe((res: string) => {
            this.toastr.warning(res);
        });
    }

    /**
     * Remove all or a single toast by id
     */
    clear(toastId?: number) {
        this.toastr.clear(toastId);
    }

}

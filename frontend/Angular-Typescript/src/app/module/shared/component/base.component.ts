import { OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

export abstract class BaseComponent implements OnDestroy {

    private subscriptions: Array<Subscription> = [];

    ngOnDestroy() {
        this.subscriptions.forEach(sub => sub.unsubscribe());
    }

    public subscription(subscription: Subscription) {
        this.subscriptions.push(subscription);
    }

}

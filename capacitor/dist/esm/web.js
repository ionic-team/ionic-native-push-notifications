var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { WebPlugin } from '@capacitor/core';
import firebase from '@firebase/app';
import '@firebase/messaging';
export class PushNotificationsPluginWeb extends WebPlugin {
    constructor() {
        super({
            name: 'PushNotifications',
            platforms: ['web']
        });
        this.messaging = null;
    }
    configure(options) {
        return new Promise((resolve, reject) => {
            if (options.mode.localeCompare('fcm') === 0) {
                if (options.senderId) {
                    firebase.initializeApp({
                        'messagingSenderId': options.senderId,
                    });
                    this.messaging = firebase.messaging();
                    navigator.serviceWorker.register('./firebase-messaging-sw.js?messagingSenderId=' + options.senderId)
                        .then((registration) => {
                        this.messaging.useServiceWorker(registration);
                        this.messaging.onTokenRefresh(() => __awaiter(this, void 0, void 0, function* () {
                            let tok = yield this.getToken();
                            this.notifyListeners('pushOnToken', tok);
                            //let ev1 = new CustomEvent('pushOnToken', {detail: tok});
                            //window.dispatchEvent(ev1);
                        }));
                        this.messaging.onMessage((payload) => {
                            //console.log('got message', payload);
                            this.notifyListeners('pushOnNotification', payload);
                            //let ev1 = new CustomEvent('pushOnNotification', {detail: payload});
                            //window.dispatchEvent(ev1);
                        });
                    });
                    resolve();
                }
                else {
                    reject('No senderId provided.');
                }
            }
            else {
                reject('No mode for push give options are: \'fcm\'');
            }
        });
    }
    ;
    getToken() {
        return new Promise((resolve, reject) => {
            // console.log('in get token');
            firebase.messaging().getToken().then((token) => {
                // console.log('messaging get token');
                resolve({ value: token });
            }).catch((e) => {
                // console.error('messagin error: ', e);
                reject(e);
            });
        });
    }
    ;
}
const PushNotifications = new PushNotificationsPluginWeb();
export { PushNotifications };
//# sourceMappingURL=web.js.map
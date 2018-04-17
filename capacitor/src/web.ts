import { WebPlugin } from '@capacitor/core';
import firebase from '@firebase/app';
import '@firebase/messaging'
import {PushConfigureOptions, PushNotificationsPlugin, PushToken} from "./definitions";

export class PushNotificationsPluginWeb extends WebPlugin implements PushNotificationsPlugin{

  messaging: any = null;

  constructor() {
    super({
      name: 'PushNotifications',
      platforms: ['web']
    });
  }

  configure(options: PushConfigureOptions): Promise<void> {
    return new Promise((resolve, reject) => {
      if(options.mode.localeCompare('fcm') === 0) {
        if(options.senderId) {
          firebase.initializeApp({
            'messagingSenderId': options.senderId,
          });
          this.messaging = firebase.messaging();
          navigator.serviceWorker.register( './firebase-messaging-sw.js?messagingSenderId=' + options.senderId )
            .then(( registration ) => {
              this.messaging.useServiceWorker( registration );
              this.messaging.onTokenRefresh(async () => {
                let tok = await this.getToken();
                this.notifyListeners('pushOnToken', tok);
              });
              this.messaging.onMessage((payload: any) => {
                this.notifyListeners('pushOnNotification', payload);
              });
            });
          resolve();
        } else {
          reject('No senderId provided.');
        }
      } else {
        reject('No mode for push give options are: \'fcm\'');
      }
    });
  };

  getToken(): Promise<PushToken> {
    return new Promise((resolve, reject) => {
      // console.log('get token');
      firebase.messaging().getToken().then((token:string) => {
        // console.log('messaging get token');
        resolve({value: token});
      }).catch((e: any) => {
        reject(e);
      });
    });
  };

}

const PushNotifications = new PushNotificationsPluginWeb();

export { PushNotifications };

- Add `firebase-messaging-sw.js` to your webDir
- Add `import 'capacitor-push-notifications';` to your app.

``` typescript
import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

import 'capacitor-push-notifications-mikes';
import {Plugins} from '@capacitor/core';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public navCtrl: NavController) {

  }

  async ionViewWillEnter() {
    try {
      let {PushNotifications} = Plugins;
      await PushNotifications.configure({
        senderId: '138006695160',
        mode: 'fcm',
      });
      let t = await PushNotifications.getToken();
      console.log(t.value);
      PushNotifications.addListener('pushOnToken', (token) => {
        console.log('Got token', token);
      });
      PushNotifications.addListener('pushOnNotification', (data) => {
        console.log('Push message data', data);
      });
    } catch(e) {
      console.error(e);
    }
  }

}
```
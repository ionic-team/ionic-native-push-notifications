import { WebPlugin } from '@capacitor/core';
import '@firebase/messaging';
import { PushConfigureOptions, PushNotificationsPlugin, PushToken } from "./definitions";
export declare class PushNotificationsPluginWeb extends WebPlugin implements PushNotificationsPlugin {
    messaging: any;
    constructor();
    configure(options: PushConfigureOptions): Promise<void>;
    getToken(): Promise<PushToken>;
}
declare const PushNotifications: PushNotificationsPluginWeb;
export { PushNotifications };

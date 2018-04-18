declare global  {
    interface PluginRegistry {
        PushNotifications?: PushNotificationsPlugin;
    }
}
export interface PushNotificationsPlugin {
    configure(options: PushConfigureOptions): Promise<void>;
    getToken(): Promise<PushToken>;
    addListener(eventName: 'pushOnToken', listenerFunc: (token: string) => void): void;
    addListener(eventName: 'pushOnNotification', listenerFunc: (data: any) => void): void;
}
export interface PushConfigureOptions {
    senderId: string;
    mode: 'fcm';
}
export interface PushToken {
    value: string;
}

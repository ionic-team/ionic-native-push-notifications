declare global {
  interface PluginRegistry {
    PushNotifications?: PushNotificationsPlugin;
  }
}

export interface PushNotificationsPlugin {
  configure(options: PushConfigureOptions): Promise<void>;
  getToken(): Promise<PushToken>;
  addListener(eventName: 'pushOnToken', listenerFunc: (token: string) => void): void;
  addListener(eventName: 'pushOnMessage', listenerFunc: (data: any) => void): void;
}

export interface PushConfigureOptions {
  [x: string]: any;
}

export interface PushToken {
  value: string;
}
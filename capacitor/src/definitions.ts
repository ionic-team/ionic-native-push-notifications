declare global {
  interface PluginRegistry {
    PushNotifications?: PushNotificationsPlugin;
  }
}

export interface PushNotificationsPlugin {
  addListener(eventName: 'pushOnToken', listenerFunc: (token: string) => void): void;
  addListener(eventName: 'pushOnMessage', listenerFunc: (data: any) => void): void;
}

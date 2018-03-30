package com.ionicframework.nativeplugins.push;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class PushNotificationsMessagingService extends FirebaseMessagingService {
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // ...

    // TODO(developer): Handle FCM messages here.
    // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
    Log.d(IonicPushNotifications.TAG, "From: " + remoteMessage.getFrom());

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      Log.d(IonicPushNotifications.TAG, "Message data payload: " + remoteMessage.getData());
    }

    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      Log.d(IonicPushNotifications.TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
    }

    IonicPushNotifications.getInstance().notifyListeners("onMessage", remoteMessage.getData());

    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
  }

  @Override
  public void onDeletedMessages() {
    Log.d(IonicPushNotifications.TAG, "Some messages deleted");
  }
}

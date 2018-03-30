package com.ionicframework.nativeplugins.push;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class IonicPushNotifications extends IonicNativePlugin {
  protected static final String TAG = "IonicPushNotifications";

  private String token = null;

  private static IonicPushNotifications _instance = null;
  public static IonicPushNotifications getInstance() {
    if (_instance == null) {
      _instance = new IonicPushNotifications();
    }
    return _instance;
  }

  private IonicPushNotifications() {
  }

  public void init(Activity activity) {
    FirebaseApp.initializeApp(activity);
    token = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Got token on launch: " + token);
  }

  public void processIntent(Intent intent) {
    Log.d(TAG, "Processing intent");
  }

  @Override
  public void addListener(String eventName, PluginEventListener listener) {
    super.addListener(eventName, listener);
    if (eventName.equals("onToken") && token != null) {
      notifyListener("onToken", listener, token);
    }
  }

  public void onNotification(PluginEventListener listener) {
    addListener("onMessage", listener);
  }

  public void onToken(PluginEventListener listener) {
    addListener("onToken", listener);
  }

  public String getToken() {
    // Get token
    token = FirebaseInstanceId.getInstance().getToken();
    return token;
  }

  public void subscribe(String topicName) {
    // Todo: support NotificationChannels in Android O
    FirebaseMessaging.getInstance().subscribeToTopic(topicName);
  }

  public void unsubscribe(String topicName) {
    FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName);
  }
}

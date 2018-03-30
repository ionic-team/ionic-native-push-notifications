package com.ionicframework.nativeplugins.push;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class PushNotificationsInstanceIdService extends FirebaseInstanceIdService {
  @Override
  public void onTokenRefresh() {
    // Get updated InstanceID token.
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(IonicPushNotifications.TAG, "Refreshed token: " + refreshedToken);

    IonicPushNotifications.getInstance().notifyListeners("onToken", refreshedToken);
  }
}

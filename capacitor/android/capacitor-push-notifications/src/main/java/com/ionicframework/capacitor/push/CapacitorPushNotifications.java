package com.ionicframework.capacitor.push;

import android.util.Log;

import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.ionicframework.nativeplugins.push.IonicNativePlugin;
import com.ionicframework.nativeplugins.push.IonicPushNotifications;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@NativePlugin(name="PushNotifications")
public class CapacitorPushNotifications extends Plugin {
  Map<String, IonicNativePlugin.PluginEventListener> registeredListeners = new HashMap<>();

  public void load() {
    IonicPushNotifications.getInstance().init(getActivity());
  }

  @PluginMethod
  public void getToken(PluginCall call) {
    String token = IonicPushNotifications.getInstance().getToken();
    JSObject data = new JSObject();
    data.put("value", token);
    call.resolve(data);
  }

  @PluginMethod
  public void subscribe(PluginCall call) {
    String topic = call.getString("topic");
    IonicPushNotifications.getInstance().subscribe(topic);
    call.resolve();
  }

  @PluginMethod
  public void unsubscribe(PluginCall call) {
    String topic = call.getString("topic");
    IonicPushNotifications.getInstance().unsubscribe(topic);
    call.resolve();
  }

  @Override
  @PluginMethod
  public void addListener(PluginCall call) {
    super.addListener(call);
    String eventName = call.getString("eventName");

    Log.d(Bridge.TAG, "Adding listener for " + eventName);
    if (eventName.equals("pushOnNotification")) {
      IonicNativePlugin.PluginEventListener listener = new IonicNativePlugin.PluginEventListener() {
        @Override
        public void onEvent(String eventName, Object data) {
          super.onEvent(eventName, data);
          Map<String, Object> dataObject = (Map<String, Object>) data;
          JSObject ret = new JSObject();

          for (Map.Entry<String, Object> field : dataObject.entrySet()) {
            ret.put(field.getKey(), field.getValue());
          }

          notifyListeners("pushOnNotification", ret);
        }
      };
      IonicPushNotifications.getInstance().addListener("onMessage", listener);
      registeredListeners.put(call.getCallbackId(), listener);
    } else if (eventName.equals("pushOnToken")) {
      IonicPushNotifications.PluginEventListener listener = new IonicNativePlugin.PluginEventListener() {
        @Override
        public void onEvent(String eventName, Object data) {
          super.onEvent(eventName, data);
          JSObject ret = new JSObject();
          String token = (String) data;
          ret.put("value", token);
          notifyListeners("pushOnToken", ret);
        }
      };

      IonicPushNotifications.getInstance().addListener("onToken", listener);
      registeredListeners.put(call.getCallbackId(), listener);
    }
  }

  @Override
  @PluginMethod
  public void removeListener(PluginCall call) {
    super.removeListener(call);

    IonicNativePlugin.PluginEventListener listener = registeredListeners.get(call.getCallbackId());
    if (listener == null) {
      call.resolve();
      return;
    }

    registeredListeners.remove(call.getCallbackId());
    String eventName = call.getString("eventName");
    IonicPushNotifications.getInstance().removeListener(eventName, listener);
  }
}

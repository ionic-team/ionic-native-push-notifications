package com.ionicframework.nativeplugins.push;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IonicNativePlugin {
  public static class PluginEventListener {
    public void onEvent(String eventName, Object data) {}
  }

  private Map<String, List<PluginEventListener>> listeners = new HashMap<>();

  public void addListener(String event, PluginEventListener e) {
    List<PluginEventListener> l = listeners.get(event);
    if (l == null) {
      l = new ArrayList<>();
    }
    l.add(e);
    listeners.put(event, l);
  }

  public void removeListener(String event, PluginEventListener e) {
    List<PluginEventListener> l = listeners.get(event);
    if (l == null) {
      return;
    }
    List<PluginEventListener> toRemove = new ArrayList<>();
    for (int i = 0; i < l.size(); i++) {
      PluginEventListener et = l.get(i);
      if (et == e) {
        toRemove.add(e);
      }
    }
    l.removeAll(toRemove);
  }

  public void notifyListeners(String event, Object data) {
    List<PluginEventListener> l = listeners.get(event);
    if (l == null) {
      return;
    }

    for (PluginEventListener e : l) {
      notifyListener(event, e, data);
    }
  }

  public void notifyListener(String event, PluginEventListener listener, Object data) {
    listener.onEvent(event, data);
  }
}

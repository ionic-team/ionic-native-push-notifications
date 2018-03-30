import Foundation
import Capacitor
import IonicNativePushNotifications

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(CAPPushNotifications)
public class CAPPushNotifications: CAPPlugin {
  @objc func configure(_ call: CAPPluginCall) {
    
  }
  
  
  
  @objc func getToken(_ call: CAPPluginCall) {
    let token = IonicPushNotifications.shared.token
    call.resolve([
      "value": token ?? ""
    ])
  }
  
  @objc public override func addListener(_ call: CAPPluginCall!) {
    super.addListener(call)
    let eventName = call.getString("eventName")
    
    if eventName == "pushOnToken" {
      IonicPushNotifications.shared.onToken { (token) in
        call.success([
          "value": token
        ])
      }
    }
    if eventName == "pushOnNotification" {
      IonicPushNotifications.shared.onNotification { (data) in
        call.success([
          "data": data
        ])
      }
    }
  }
}

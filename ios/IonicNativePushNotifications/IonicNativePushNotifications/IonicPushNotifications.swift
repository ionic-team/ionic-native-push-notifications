import Foundation
import UserNotifications
import FirebaseInstanceID
import FirebaseCore
import FirebaseMessaging

public typealias PushSetupOptions = [String:Any?]
public typealias PushSetupCallback = (Error?) -> Void
public typealias PushOnRegisterCallback = (String) -> Void
public typealias PushOnNotificationCallback = ([AnyHashable:Any]) -> Void

public enum IonicPushNotificationsError : Error {
  case permissionDenied
  case unknownError
}

public class IonicPushNotifications: NSObject, UNUserNotificationCenterDelegate, MessagingDelegate {
  let DEFAULT_PROMPT_FOR_PERMISSIONS = true
  let EVENT_REGISTER_ERROR = "pushRegisterError"
  let gcmMessageIDKey = "gcm.message_id"
  
  var onRegisterHandler: PushOnRegisterCallback?
  var onNotificationHandler: PushOnNotificationCallback?
  
  public var token: String?
  
  private static var _instance: IonicPushNotifications?
  public static var shared: IonicPushNotifications {
    get {
      if IonicPushNotifications._instance == nil {
        IonicPushNotifications._instance = IonicPushNotifications()
      }
      return IonicPushNotifications._instance!
    }
  }
  
  public override init() {}
  
  /**
   * Configure the Push Notifications instance. This should be called in applicationDidFinishLaunching
   * in your AppDelegate
   */
  public static func configure() {
    UNUserNotificationCenter.current().delegate = IonicPushNotifications.shared
    Messaging.messaging().delegate = IonicPushNotifications.shared
    FirebaseApp.configure()
  }
  
  public func onRegister(handler: @escaping PushOnRegisterCallback) {
    onRegisterHandler = handler
  }
  
  public func onNotification(handler: @escaping PushOnNotificationCallback) {
    onNotificationHandler = handler
  }
  
  public func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String) {
    print("Firebase registration token: \(fcmToken)")
    token = fcmToken
    onRegisterHandler?(token!)
  }

  // Receive displayed notifications for iOS 10 devices.
  public func userNotificationCenter(_ center: UNUserNotificationCenter,
                                     willPresent notification: UNNotification,
                                     withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
    let userInfo = notification.request.content.userInfo
    
    // With swizzling disabled you must let Messaging know about the message, for Analytics
    // Messaging.messaging().appDidReceiveMessage(userInfo)
    
    // Print message ID.
    if let messageID = userInfo[gcmMessageIDKey] {
      print("Message ID: \(messageID)")
    }
    
    // Print full message.
    print(userInfo)
    
    onNotificationHandler?(userInfo)
    
    // Change this to your preferred presentation option
    completionHandler([])
  }
  
  public func userNotificationCenter(_ center: UNUserNotificationCenter,
                              didReceive response: UNNotificationResponse,
                              withCompletionHandler completionHandler: @escaping () -> Void) {
    let userInfo = response.notification.request.content.userInfo
    // Print message ID.
    if let messageID = userInfo[gcmMessageIDKey] {
      print("Message ID: \(messageID)")
    }
    
    // Print full message.
    print(userInfo)
    
    completionHandler()
  }

  
  public func setup(options: PushSetupOptions = [:], completion: @escaping PushSetupCallback) {
    self.setupCheckPermissions(options, completion)
  }
  
  public func checkPermissions(completion: @escaping (UNAuthorizationStatus) -> Void) {
    UNUserNotificationCenter.current().getNotificationSettings { (settings) in
      completion(settings.authorizationStatus)
    }
  }
  
  /**
   * Before registering, check permissions and, optionally, prompt for permissions (or error out)
   */
  func setupCheckPermissions(_ options: PushSetupOptions, _ completion: @escaping PushSetupCallback) {
    let promptForPermissions = options["promptForPermissions"] as? Bool ?? DEFAULT_PROMPT_FOR_PERMISSIONS
    
    checkPermissions { (authorizationStatus) in
      if authorizationStatus == .authorized {
        self.setupRegister()
      } else if authorizationStatus == .denied {
        let err = IonicPushNotificationsError.permissionDenied
        completion(err)
        return
      } else if authorizationStatus != .authorized {
        if promptForPermissions {
          self.requestPushPermissions(completion: { (granted) in
            if granted {
              self.setupRegister()
            } else {
              let err = IonicPushNotificationsError.permissionDenied
              completion(err)
            }
          })
        } else {
          let err = IonicPushNotificationsError.unknownError
          completion(err)
        }
      }
    }
  }
  
  func setupRegister() {
    print("Registering...")
    DispatchQueue.main.async {
      // For iOS 10 display notification (sent via APNS)
      
      let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
      UNUserNotificationCenter.current().requestAuthorization(options: authOptions)  {
        (granted, error) in
        print("Requested auth", granted, error)
      }
      
      UIApplication.shared.registerForRemoteNotifications()
    }
  }
  
  
  func requestPushPermissions(completion: @escaping (Bool) -> Void) {
    UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) {
      (granted, error) in
      if !granted || error != nil {
        completion(false)
      } else {
        completion(granted)
      }
    }
  }
}

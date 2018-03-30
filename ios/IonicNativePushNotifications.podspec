Pod::Spec.new do |s|
  s.name = 'IonicNativePushNotifications'
  s.version = '0.0.2'
  s.summary = 'Push notification plugin for Capacitor and Cordova'
	s.social_media_url = 'http://twitter.com/ionicframework'
  s.license = 'MIT'
  s.homepage = 'https://ionicframework.com/'
  s.ios.deployment_target  = '10.0'
  s.authors = { 'Ionic Team' => 'hi@ionicframework.com' }
  s.source = { :git => 'https://github.com/ionic-team/ionic-native-push-notifications.git', :tag => s.version.to_s }
  s.source_files = 'IonicNativePushNotifications/IonicNativePushNotifications/**/*.{swift,h,m}'
  s.static_framework = true
  s.dependency 'FirebaseMessaging'
end


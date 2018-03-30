set -e

export BINTRAY_PKG_VERSION=0.0.8

cd android

./gradlew clean build -b ionicnativepushnotifications/build.gradle bintrayUpload -PbintrayUser=$BINTRAY_USER -PbintrayKey=$BINTRAY_KEY -PdryRun=false

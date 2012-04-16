mvn -Dandroid.sdk.path=/Users/jlong/bin/android-sdk-macosx
mvn android:emulator-start 
echo "Run mvn android:deploy when the emulator's started" 
#mvn android:deploy 
#adb logcat
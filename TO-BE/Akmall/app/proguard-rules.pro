# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/break200/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class net.daum.mf.speech.**
-keepclassmembers class net.daum.mf.speech.** { *; }
-keep enum net.daum.mf.speech.**
-keepclassmembers enum net.daum.mf.speech.** { *; }
-keep interface net.daum.mf.speech.**
-keepclassmembers interface net.daum.mf.speech.** { *; }

#-keepattributes SourceFile,LineNumberTable

-keep class com.ak.android.akmall.utils.http.** {
   public *;
}

-keep class com.ak.android.akmall.utils.json.** {
   public *;
}

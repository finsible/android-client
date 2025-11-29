# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}

# ObjectBox rules - prevent obfuscation of entity classes and generated code
-keep class io.objectbox.BoxStore { *; }
-keep class io.objectbox.Box { *; }
-keep class io.objectbox.Cursor { *; }
-keep class io.objectbox.exception.* { *; }
-keep interface io.objectbox.annotation.* { *; }
-keep @io.objectbox.annotation.Entity class * { *; }
-keep class * extends io.objectbox.EntityInfo { *; }
-keep class com.itsjeel01.finsiblefrontend.data.local.entity.MyObjectBox { *; }
-keep class com.itsjeel01.finsiblefrontend.data.local.entity.**Entity { *; }
-keep class com.itsjeel01.finsiblefrontend.data.local.entity.**Entity_ { *; }
-keep class * implements io.objectbox.converter.PropertyConverter { *; }
-keepattributes *Annotation*


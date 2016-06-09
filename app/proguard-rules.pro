# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\miguelangel\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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
-dontwarn com.squareup.okhttp.**
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn com.google.android.gms.maps.**
-dontwarn android.support.v4.**
-keep class android.support.v4.internal.** { *; }
-keep interface android.support.v4.internal.** { *; }

-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application

-dontwarn com.squareup.**
-keep class com.squareup.** { *; }
-keep interface com.squareup.** { *; }


-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep class io.realm.internal.** { *; }
-keep @io.realm.internal.Keep class * { *; }
-keep @io.realm.internal class *
-dontwarn javax.**
-dontwarn io.realm.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class com.telcel.imk.view.ViewConfiguracao {
    public <methods>;
}

-keep class !android.support.v7.internal.view.menu.**, ** { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn com.google.android.gms.maps.**
-dontwarn android.support.v4.**
-keep class android.support.v4.internal.** { *; }
-keep interface android.support.v4.internal.** { *; }

-dontwarn butterknife.internal.**

-keep class **$$ViewInjector { *; }

-keepnames class * { @butterknife.InjectView *;}

-dontwarn butterknife.Views$InjectViewProcessor
-keep class org.spongycastle.** { *; }
-dontwarn org.spongycastle.**

-keep class com.itextpdf.** { *; }
-dontwarn com.itextpdf.**
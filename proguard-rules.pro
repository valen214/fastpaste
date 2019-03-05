-basedirectory D:\GoogleDrive\sync\main-custom-project\code\android\fastpaste
-injar bin\app.apk
-outjar bin\app.pro.apk
-libraryjars D:\software\android_sdk\platforms\android-28\android.jar

# Kotlin
-keep class kotlin.** { *; }
-dontnote kotlin.internal.PlatformImplementationsKt
-dontnote kotlin.reflect.jvm.internal.**
# -dontwarn kotlin.**
# https://medium.com/androiddevelopers/practical-proguard-rules-examples-5640a3907dc9
-assumenosideeffects class kotlin.jvm.internal.Intrinsics{
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
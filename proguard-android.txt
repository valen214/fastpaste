-basedirectory D:\GoogleDrive\sync\main-custom-project\code\android\fastpaste
-injar bin\app.apk
-outjar bin\app.pro.apk
-libraryjars D:\software\android_sdk\platforms\android-28\android.jar

-keep class kotlin.** { *; }
# -dontwarn kotlin.**
-assumenosideeffects class kotlin.jvm.internal.Intrinsics{
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
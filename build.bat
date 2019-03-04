
@echo off

Goto :start
https://medium.com/@authmane512/how-to-build-an-apk-from-command-line-without-ide-7260e1e22676

https://developer.android.com/studio/build/building-cmdline

https://www.hanshq.net/command-line-android.html

:start

set "cwd=%cd%"
set "PROJ=D:\GoogleDrive\sync\main-custom-project\code\android\app"
cd /D "%PROJ%"

<nul set /p=[94m
call :sep
echo.
echo ^> build.bat %*[0m
if "%~1 %~2"=="run only" (
    <nul set /p=[94m
    echo.
    call :sep
    echo.[0m
    Goto :run
)

:: set "FILE_ROOT=%~dp0"
:: cd /D "%FILE_ROOT%"
set "gradle_cmd=gradle --build-cache -w assemble lint"
::  --warning-mode none
<nul set /p=[94m
echo ^> %gradle_cmd%
echo.
echo.
call :sep
echo.[0m
cmd /c %gradle_cmd%
if %errorlevel% neq 0 (
    echo errorlevel: %errorlevel%
    echo compile error: stop executing
    call :sep
    echo.
    Goto :end
)

echo.
echo.
call :sep
echo.
echo lint report:
echo file:///D:/workspace/main-custom-project/code/android/app/build/reports/lint-results.html#GradleOverrides
echo.
call :sep

if "%~1"=="run" (
    Goto :run
)
Goto :end

:: legacy

if "%~1 %~2"=="run only" (
    Goto :run
)
if "%USE_KOTLIN%"=="" (
    set USE_KOTLIN=true
)



echo project root: %PROJ%
echo.
if not "%~1"=="run" (
    echo build without executing
    echo.
)
echo packing resources and generating R.java
echo.

:: https://chromium.googlesource.com/android_tools/+/d8a5bfea861dfbacd9a74275c00561f7bb27d6e3/sdk/tools/ant/build.xml
:: cmd /c aapt package -f -m ^
::         -J "%PROJ%/src" ^
::         -M "%PROJ%/AndroidManifest.xml" ^
::         -S "%PROJ%/res" ^
::         -I "%ANDROID_JAR_PATH%"

cmd /c aapt2 compile %PROJ%\res\layout\activity_main.xml -o compiled\res\
cmd /c aapt2 compile %PROJ%\res\values\strings.xml -o compiled\res\
cmd /c aapt2 compile %PROJ%\res\values\colors.xml -o compiled\res\
cmd /c aapt2 link ^
        -o %PROJ%\bin\app.unaligned.apk ^
        -I %ANDROID_JAR_PATH% ^
        --manifest %PROJ%\AndroidManifest.xml ^
        --java %PROJ%\src ^
        %PROJ%\compiled\res\layout_activity_main.xml.flat ^
        %PROJ%\compiled\res\values_strings.arsc.flat ^
        %PROJ%\compiled\res\values_colors.arsc.flat ^




call :sep
:: C:\Program Files\Android\Android Studio\gradle\m2repository\org\jetbrains\kotlin\kotlin-android-extensions\1.3.20
:: C:\Program Files\Android\Android Studio\gradle\m2repository\org\jetbrains\kotlin\kotlin-gradle-plugin\1.3.20
echo compiling...
echo.
if %USE_KOTLIN%==true (
    cmd /c kotlinc -Werror -d compiled\src -cp "src;%ANDROID_JAR_PATH%;lib" ^
            src/com/randommain/fastpaste/*.kt ^
            src/com/randommain/fastpaste/*.java
) else (
    javac -d compiled\src -bootclasspath "%ANDROID_JAR_PATH%" ^
            src/com/randommain/fastpaste/*.java
)

if %errorlevel% neq 0 (
    echo errorlevel: %errorlevel%
    echo compile error: stop executing
    call :sep
    echo.
    Goto :end
) else (
    echo compiling finished
    call :sep
    echo packing classes.dex
    echo.
    cmd /c d8 ^
            %PROJ%\compiled\src\com\randommain\fastpaste\* ^
            %PROJ%\lib\kotlin-stdlib.jar ^
            --lib "%ANDROID_JAR_PATH%" ^
            --output %PROJ%\compiled\classes
::            --classpath "%PROJ%\lib\kotlin-reflect.jar" ^

    echo adding 'classes.dex' to apk
    cmd /c aapt add "%PROJ%\bin\app.unaligned.apk" ^
            "%PROJ%\compiled\classes\classes.dex"

    echo.

    echo aligning and signing apk
    :: keytool -genkeypair -validity 365 -keystore bin\key.keystore -keyalg RSA -keysize 2048
    cmd /c zipalign -f 4 "%PROJ%/bin/app.unaligned.apk" "%PROJ%/bin/app.apk"

    set "password=TemporaryPassword"
    cmd /c apksigner sign --ks bin\key.keystore ^
            --ks-pass pass:%password% ^
            --key-pass pass:%password% ^
            "%PROJ%/bin/app.apk"

    echo.
)


:: https://docs.nativescript.org/core-concepts/android-runtime/debug/debug-native

if not "%~1"=="run" (
    Goto :end
)

:run
echo running with adb
:: nox_adb connect 127.0.0.1:62001
:: adb devices
set "PROJECT_PACKAGE=com.randommain.fastpaste"
set "ADB_DEVICE=127.0.0.1:62001"
cmd /c adb logcat -c
cmd /c adb -s %ADB_DEVICE% uninstall %PROJECT_PACKAGE%
cmd /c adb -s %ADB_DEVICE% install -r bin/app.apk
cmd /c adb -s %ADB_DEVICE% shell am start -n %PROJECT_PACKAGE%/.MainActivity
call :sep
echo.
echo program output ^> adb logcat MyActivity:D AndroidRuntime:E *:S
echo.
call :sep
cmd /c adb logcat MyActivity:D AndroidRuntime:E *:S

:: adb forward tcp:40000 localabstract:com.randommain.fastpaste-inspectorServer
:: adb forward tcp:40001 localabstract:chrome_devtools_remote
:: chrome-devtools://devtools/bundled/inspector.html?experiments=true&ws=localhost:8080
:: adb forward --remove tcp:40000
:: adb forward --remove-all
:: adb logcat
:: https://stuff.mit.edu/afs/sipb/project/android/docs/tools/debugging/debugging-log.html#viewingStd
:: severity level: VDIWEFS
:: adb logcat ActivityManager:I MyActivity:D *:S stdout:I stderr:I

:: adb root
:: adb shell stop
:: adb shell setprop log.redirect-stdio true
:: adb shell start
:: lint --sources "%PROJ%\src" --libraries "%PROJ%\lib" --resources "%PROJ%\res" --classpath "%PROJ%\obj" %cd%
Goto :end

:sep
echo #####################################^
###########################################

:end
cd /D "%cwd%"
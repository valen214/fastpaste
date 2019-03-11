
@echo off

Goto :start
https://medium.com/@authmane512/how-to-build-an-apk-from-command-line-without-ide-7260e1e22676

https://developer.android.com/studio/build/building-cmdline

https://www.hanshq.net/command-line-android.html

:start

:init
set "cwd=%cd%"
set "PROJ=D:\GoogleDrive\sync\main-custom-project\code\android\app"
cd /D "%PROJ%"

set "ARG_RUN=false"
set "ARG_RUN_ONLY=false"
set "ARG_PROJECT="

:parse_arg
if not "%~1"=="" (
    if "%~1"=="run" (
        set "ARG_RUN=true"
        if "%~2"=="only" (
            set "ARG_RUN_ONLY=true"
            shift /1
        )
    ) else if "%~1"=="" (
        echo. >nul
    ) else if exist "%~1" (
        set "ARG_PROJECT=%~1"
    )


    shift /1
    Goto :parse_arg
)

if "%ARG_PROJECT%"=="" (
    set "ARG_PROJECT=randompicker"
)
:end_parse_arg

set "PROJECT_PACKAGE=com.basicapp.%ARG_PROJECT%"

<nul set /p=[94m
call :sep
echo.
echo ^> build.bat %*[0m
if "%ARG_RUN_ONLY%"=="true" (
    <nul set /p=[94m
    echo.
    call :sep
    echo.[0m
    Goto :run
)

:: set "FILE_ROOT=%~dp0"
:: cd /D "%FILE_ROOT%"
:: debug variant is skipped in build.gradle
set "gradle_cmd=gradle --build-cache -q :%ARG_PROJECT%:assembleDebug lint"
:: -Pandroid.optional.compilation=INSTANT_DEV -Pandroid.injected.build.api=28"
::  --offline
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

if not "%ARG_RUN%"=="true" (
    Goto :end
)

:run
echo running with adb
:: nox_adb connect 127.0.0.1:62001
:: adb devices
set "ADB_DEVICE=127.0.0.1:62001"
cmd /c adb logcat -c
cmd /c adb -s %ADB_DEVICE% uninstall %PROJECT_PACKAGE%
cmd /c adb -s %ADB_DEVICE% install -r bin/%ARG_PROJECT%-debug.apk
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
@echo off
title EasyShop - compilar e instalar (deixe esta janela aberta)
cd /d "%~dp0"
set "LOG=%~dp0compilar-log.txt"
if not defined JAVA_HOME if exist "C:\Program Files\Android\Android Studio\jbr" set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
if not defined ANDROID_HOME set "ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk"
echo === COMPILAR %DATE% %TIME% === > "%LOG%"
echo [1/2] Compilando o app (a primeira vez pode levar 10 minutos)...
call gradlew.bat assembleDebug --console=plain >> "%LOG%" 2>&1
if not exist app\build\outputs\apk\debug\app-debug.apk (echo APK_NAO_GERADO >> "%LOG%" & echo DEU ERRO - avise o Claude & echo === FIM === >> "%LOG%" & pause & exit /b 1)
echo APK_OK >> "%LOG%"
if not exist apk mkdir apk
copy /y app\build\outputs\apk\debug\app-debug.apk apk\EasyShop.apk >> "%LOG%"
echo [2/2] Instalando no emulador (se estiver aberto)...
"%ANDROID_HOME%\platform-tools\adb.exe" install -r apk\EasyShop.apk >> "%LOG%" 2>&1
"%ANDROID_HOME%\platform-tools\adb.exe" shell am start -n com.easyshop.poliglota/.ui.MainActivity >> "%LOG%" 2>&1
echo === FIM === >> "%LOG%"
echo PRONTO! APK em apk\EasyShop.apk
pause

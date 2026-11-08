@echo off
title EasyShop - enviar para o GitHub
cd /d "%~dp0"
set "LOG=%~dp0enviar-github-log.txt"
echo === ENVIAR %DATE% %TIME% === > "%LOG%"
if not exist .git (git init -b main >> "%LOG%" 2>&1)
git config core.autocrlf true >> "%LOG%" 2>&1
git remote get-url origin >nul 2>&1 || git remote add origin https://github.com/benimaru0101-collab/easyshop-poliglota.git
git add -A >> "%LOG%" 2>&1
git commit -m "EasyShop: persistencia poliglota (Room/SQLite + ObjectBox) em camadas" >> "%LOG%" 2>&1
git push -u origin main >> "%LOG%" 2>&1
if errorlevel 1 (echo PUSH_ERRO >> "%LOG%" & echo DEU ERRO - avise o Claude) else (echo PUSH_OK >> "%LOG%" & echo PRONTO! Codigo enviado para o GitHub)
echo === FIM === >> "%LOG%"
pause

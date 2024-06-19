@echo off

setlocal
cd %~dp0\..\..

echo on
gradlew build --warning-mode all

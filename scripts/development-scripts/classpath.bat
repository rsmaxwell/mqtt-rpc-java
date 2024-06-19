@echo on
setLocal EnableDelayedExpansion

cd %~dp0\..\..

set CLASSPATH="
for /R ./runtime %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"
echo !CLASSPATH!
 
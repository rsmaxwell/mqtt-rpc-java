@echo off
setLocal EnableDelayedExpansion

cd %~dp0\..\..

set CLASSPATH="
for /R ./build/libs %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
for /R ./runtime %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!;build\classes\java\test"
echo %CLASSPATH%
 
echo on
java -classpath %CLASSPATH% com.rsmaxwell.rpc.CalculatorTest --username richard --password secret --operation mul --param1 10 --param2 5

@echo off
setlocal

set "SRC=src\com\highfi\dashboard\HighFiDashboardApp.java"
set "OUT=out"
set "MAIN=com.highfi.dashboard.HighFiDashboardApp"

if /I "%~1"=="build" goto BUILD_ONLY

echo [1/2] Compiling PulseBoard...
javac -d "%OUT%" -sourcepath src "%SRC%"
if errorlevel 1 goto BUILD_FAILED

echo [2/2] Launching PulseBoard...
java -cp "%OUT%" %MAIN%
goto DONE

:BUILD_ONLY
echo Compiling PulseBoard...
javac -d "%OUT%" -sourcepath src "%SRC%"
if errorlevel 1 goto BUILD_FAILED
echo Build successful.
goto DONE

:BUILD_FAILED
echo Build failed. Ensure Java is installed and available on PATH.
exit /b 1

:DONE
endlocal

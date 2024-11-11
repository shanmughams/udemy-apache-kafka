@echo off
setlocal

:: Define the target directory
set "target_dir=C:\kafka_2.13-3.8.1\docker-mount-points\data"

:: Ensure the target directory exists
if exist "%target_dir%" (
    echo Deleting all files and folders in %target_dir%...

    :: Delete all files in the target directory
    del /q /f "%target_dir%\*.*"

    :: Delete all subdirectories in the target directory
    for /d %%D in ("%target_dir%\*") do rd /s /q "%%D"

    echo Deletion complete.
) else (
    echo Directory %target_dir% does not exist. Creating it...
    mkdir "%target_dir%"
)

:: Create the new directories
echo Creating kafka1, kafka2, kafka3, and zookeeper directories...
mkdir "%target_dir%\kafka1"
mkdir "%target_dir%\kafka2"
mkdir "%target_dir%\kafka3"
mkdir "%target_dir%\zookeeper"

cd D:\me\learn\udemy\kafka-springboot\kafkaproducer\docker
docker-compose -f docker-compose-multi-broker.yml up -d

echo All tasks completed.

endlocal

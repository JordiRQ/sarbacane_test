@echo off

cd %cd%/recipient-service
call mvn clean
cd %cd%/../csv-processor
call mvn clean
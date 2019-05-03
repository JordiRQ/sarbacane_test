@echo off

cd %cd%/recipient-service
call mvn install
cd %cd%/../csv-processor
call mvn install
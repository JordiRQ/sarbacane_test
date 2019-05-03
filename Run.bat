@echo off

start /d %~dp0 java -jar %cd%/recipient-service/recipient-service/target/recipient-service-1.0.0-SNAPSHOT.jar
start /d %~dp0 java -jar -Dspring.profiles.active=dev %cd%/csv-processor/target/csv-processor-1.0.0-SNAPSHOT.jar
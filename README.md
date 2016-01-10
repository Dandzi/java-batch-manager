# Java Batch Manager
Java Batch Manager is web application for managing batch jobs defined in JSR-352 and Spring Batch. Application can:
-start, stop and restart jobs
-show history of jobs
-do this via REST API

## Installation
1. Import this project to Eclipse IDE as existing maven project.
2. You should have instance of WildFly AS, on which this app will be deployed.
3. Running app: Right click on project->select Run as...->select Run on server-> select existing WildFly server

Application should be available at url:
http://localhost:8080/java-batch-manager/launchable-jobs

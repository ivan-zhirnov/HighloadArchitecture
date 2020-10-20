#!/bin/bash
mvn clean package
java -jar target/server.jar

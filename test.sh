#!/usr/local/bin/bash

javac -d dist -cp junit5:src:junit-platform-console.jar src/Tests.java

java -jar junit-platform-console.jar --class-path dist --scan-class-path

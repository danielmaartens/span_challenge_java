#!/usr/local/bin/bash

javac -d dist -cp src:junit-platform-console.jar src/Tests.java

cd dist

java Main
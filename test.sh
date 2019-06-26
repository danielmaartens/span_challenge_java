#!/usr/local/bin/bash

rm -rf build/test >/dev/null
rm mkdir build/test

javac -d build/test -cp junit5:src:junit-platform-console.jar src/test/Tests.java
java -jar junit-platform-console.jar --class-path build/test --scan-class-path


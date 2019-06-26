#!/usr/local/bin/bash

rm -rf build  >/dev/null
mkdir build

javac -d build -cp src src/main/Main.java

cd build

java main.Main
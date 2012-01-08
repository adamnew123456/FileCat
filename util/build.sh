#!/bin/bash
if ls *.class &> /dev/null; then
    rm *.class
fi

if [ "$1" != "clean" ]; then
    javac -g Networking.java
    javac -g CLI.java
fi

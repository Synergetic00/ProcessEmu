#!/bin/bash
javac -d bin -cp "lib/*" $(find . -name '*.java')
echo "Compiled"
java -cp 'bin/;lib/*' main.Main
#!/bin/bash

set PATH_TO_FX="./javafx/lib"

SCRIPTPATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

vmargs="--module-path $SCRIPTPATH/lib --add-modules javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.web --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED"


find ./src -name "*.java" > sources.txt
javac -d ./bin -classpath '${CLASSPATH}' @sources.txt
#java $vmargs -classpath ./bin main.Main -jar ./lib/*.jar 
java -jar --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml
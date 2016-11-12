#!/bin/bash

TARGET_DIR="target/scala-2.12/classes"
if [[ $OSTYPE == "darwin*" ]]; then
    JAVA_HOME=$(/usr/libexec/java_home)
fi
sbt clean compile

if [[ $1 != "quick" ]] ; then
    echo "./runGame.sh: running tests"
    sbt test
fi

./bin/halite -d "30 30" "scala -cp $TARGET_DIR slack.endofthe.marcel.MyBot" "scala -cp $TARGET_DIR slack.endofthe.marcel.RandomBot"

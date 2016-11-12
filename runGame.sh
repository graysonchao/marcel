#!/bin/bash

CLASSPATH="target/pack/lib/*"

if [[ $OSTYPE == "darwin*" ]]; then
    JAVA_HOME=$(/usr/libexec/java_home)
fi

sbt clean compile

if [[ $1 != "quick" ]] ; then
    echo "$0: running tests"
    sbt test
fi

echo "$0: packaging dependencies in $CLASSPATH"
sbt pack

./bin/halite -d "30 30" "scala -cp \"$CLASSPATH\" slack.endofthe.marcel.MyBot" "scala -cp \"$CLASSPATH\" slack.endofthe.marcel.RandomBot"

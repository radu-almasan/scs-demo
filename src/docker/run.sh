#!/bin/bash

while read -r line; do declare "${line?}"; done < /home/app/properties

JAVA_OPTS=${JAVA_OPTS:-""}
echo "JAVA_OPTS=$JAVA_OPTS"

java $JAVA_OPTS -jar /home/app/"$name-$version".jar


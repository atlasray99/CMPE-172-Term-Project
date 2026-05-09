#!/bin/sh
# Maven Wrapper script
# Adapted from https://github.com/apache/maven-wrapper

set -e

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

if [ -f "$MAVEN_WRAPPER_PROPERTIES" ]; then
    MAVEN_URL=$(grep 'distributionUrl' "$MAVEN_WRAPPER_PROPERTIES" | cut -d'=' -f2- | tr -d '\r')
fi

MAVEN_URL="${MAVEN_URL:-https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip}"

MAVEN_HOME="$HOME/.m2/wrapper/dists/apache-maven-3.9.6"

if [ ! -f "$MAVEN_HOME/bin/mvn" ]; then
    echo "Downloading Maven..."
    mkdir -p "$MAVEN_HOME"
    TMP_FILE="$(mktemp)"
    curl -sL "$MAVEN_URL" -o "$TMP_FILE"
    unzip -qo "$TMP_FILE" -d "$HOME/.m2/wrapper/dists/"
    rm -f "$TMP_FILE"
    # Handle nested directory from zip
    if [ ! -f "$MAVEN_HOME/bin/mvn" ] && [ -d "$HOME/.m2/wrapper/dists/apache-maven-3.9.6" ]; then
        true
    fi
fi

exec "$MAVEN_HOME/bin/mvn" "$@"

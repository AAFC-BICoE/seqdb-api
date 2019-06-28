#!/bin/bash

# Get the app version from pom.xml.
MVN_VERSION=$(mvn -q \
    -Dexec.executable=echo \
    -Dexec.args='${project.version}' \
    --non-recursive \
    exec:exec)

# The name and tag of the docker image. "dev-" is prepended to the tag.
DOCKER_IMAGE_FULLNAME="$DOCKER_IMAGE":dev-"$MVN_VERSION"

# Build the image.
docker build -t "$DOCKER_IMAGE_FULLNAME" .

# Login to docker account.
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

# Push the image to Docker Hub.
docker push "$DOCKER_IMAGE_FULLNAME"

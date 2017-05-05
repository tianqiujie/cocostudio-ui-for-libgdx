#!/bin/bash

if [[ $GDX_VERSION == '1.9.6' ]];
then
    echo 'Check coverage...'
    ./gradlew test jacoco
    bash <(curl -s https://codecov.io/bash)
fi

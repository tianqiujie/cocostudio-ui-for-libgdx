#!/bin/bash

if [[ $GDX_VERSION == '1.9.6' ]];
then
    echo 'Check coverage...'
    ./gradlew cobertura coveralls
fi

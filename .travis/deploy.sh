#!/bin/bash

if [[ $TRAVIS_PULL_REQUEST == 'false' && $TRAVIS_REPO_SLUG == 'varFamily/cocos-ui-libgdx' && $GDX_VERSION == '1.9.6' && $TRAVIS_BRANCH == 'master' ]];
then
    echo 'Upload snapshot to maven central'
    ./gradlew uploadArchives
    echo 'Trigger jitpack snapshot build'
    curl -v https://jitpack.io/com/github/varFamily/cocos-ui-libgdx/-SNAPSHOT
fi

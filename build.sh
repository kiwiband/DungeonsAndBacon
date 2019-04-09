#! /bin/bash

for hw in $(find . -maxdepth 1 -mindepth 1 -type d) ; do
    if [ -f "$hw/build.gradle" ] ; then
        echo "processing $hw"
        cd "$hw" && gradle check && cd .. || { echo "check failed" ; exit 1 ; }
        echo "finishing $hw"
    fi
done
#!/bin/bash -e

# This script is taken from https://github.com/junit-team/junit5/pull/2217

rm -rf checksums/
mkdir checksums/

export SOURCE_DATE_EPOCH=$(date +%s)

function calculate_checksums() {
    OUTPUT=checksums/$1

    ./gradlew --no-build-cache clean assemble

    find ./build -name '*.jar' \
        | grep '/build/libs/' \
        | grep --invert-match 'javadoc' \
        | sort \
        | xargs sha256sum > ${OUTPUT}
}


calculate_checksums checksums-1.txt
calculate_checksums checksums-2.txt

diff checksums/checksums-1.txt checksums/checksums-2.txt

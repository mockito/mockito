# Remove lock file to prevent caching problems when rerunning the benchmark
rm .gradle/2.14.1/taskArtifacts/cache.properties.lock
# Run the actual benchmark
./gradlew jmh

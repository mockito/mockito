plugins {
    java
    id("org.gradle.test-retry")
}

tasks.test {
    retry {
        // Concurrent tests can be flaky, so keep the existing single-retry policy.
        maxRetries.set(1)
        failOnPassedAfterRetry.set(false)
        failOnSkippedAfterRetry.set(false)
    }
}

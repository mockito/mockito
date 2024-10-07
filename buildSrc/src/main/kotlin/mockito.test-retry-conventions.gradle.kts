import java.util.concurrent.ConcurrentSkipListSet

plugins {
    java
}

/*
    Plugin that retries failed tests.
    Mockito has concurrent API and those tests and inherently flaky.
    We decided to use retries to stabilize (conscious team choice)
    Long term, we can evolve retry-test script plugin to a binary plugin or make it more robust

    Plugin adds 'retryTest' task that runs tests that failed during the execution of 'test' task.
*/
tasks {
    val retryTest by registering(Test::class) {
        description = "Retries failed tests (if present)"
        outputs.upToDateWhen { false } //we want to always run flaky tests because they are flaky
        isEnabled = false // toggled on by `test` task if there are failed tests

        // re-use same parameters
        testClassesDirs = test.map { it.testClassesDirs }.get()
        classpath = test.map { it.classpath }.get()
        isScanForTestClasses = test.map { it.isScanForTestClasses }.get()
        include(test.map { it.includes }.get())

        // logging handled by test-logger plugin

        doFirst {
            logger.lifecycle("[retryTest] retrying ${filter.includePatterns.size} test(s).")
        }
        addTestListener(object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) {}
            override fun beforeTest(testDescriptor: TestDescriptor) {}
            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
            override fun afterSuite(descriptor: TestDescriptor, result: TestResult) {
                descriptor.parent ?: return  // root
                if (result.failedTestCount > 0) {
                    logger.lifecycle("\n[retryTest] retried ${filter.includePatterns.size} test(s), $result.failedTestCount still failed.")
                } else {
                    logger.lifecycle("\n[retryTest] ${filter.includePatterns.size} test(s) were retried successfully:\n  ${filter.includePatterns.joinToString("\n  ")}")
                }
            }
        })
    }

    test {
        finalizedBy(retryTest)
        val failedTests = ConcurrentSkipListSet<String>()

        addTestListener(object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) {}
            override fun beforeTest(testDescriptor: TestDescriptor) {}

            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                if (!testDescriptor.isComposite /* e.g. is not a parent */ && result.failedTestCount > 0) {
                    //adding fully qualified test name, dropping "()" from the method name
                    failedTests.add("${testDescriptor.className}.${testDescriptor.name.replace("\\(\\)", "")}")
                }
            }

            override fun afterSuite(descriptor: TestDescriptor, result: TestResult) {
                descriptor.parent ?: return  // root
                val failuresReport = layout.buildDirectory.file("$name-failures.txt").get().asFile
                val deletion = !failuresReport.exists() || failuresReport.delete()

                val reportPath = rootProject.relativePath(failuresReport)
                if (!deletion) {
                    throw GradleException("Problems deleting failures file: $reportPath. Please delete manually and retry.")
                }

                if (failedTests.isNotEmpty()) {
                    failuresReport.writeText(failedTests.joinToString("\n"))
                    logger.lifecycle("\n[retryTest] wrote ${failedTests.size} failed tests to: $reportPath")
                    logger.info("[retryTest] all failed tests:\n  ${failedTests.joinToString("\n  ")}")
                    retryTest.get().isEnabled = true
                    retryTest.get().filter.setIncludePatterns(*failedTests.toTypedArray())
                    ignoreFailures = true
                } else {
                    logger.info("\n[retryTest] There are no failed tests, '$reportPath' file was deleted (if it existed).")
                }
            }
        })
    }
}


package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mockito;
import org.mockito.internal.runners.util.FailureDetecter;

/**
 * Created by sfaber on 7/22/16.
 */
public class StrictRunner implements RunnerImpl {

    private final Class<?> testClass;
    private final RunnerImpl runner;
    private boolean filterRequested;

    /**
     * @param runner - the runner to wrap around
     * @param testClass - for reporting purposes
     */
    public StrictRunner(RunnerImpl runner, Class<?> testClass) {
        this.runner = runner;
        this.testClass = testClass;
    }

    public void run(RunNotifier notifier) {
        //TODO need to be able to opt in for full stack trace instead of just relying on the stack trace filter
        UnnecessaryStubbingsReporter reporter = new UnnecessaryStubbingsReporter();
        FailureDetecter listener = new FailureDetecter();

        Mockito.framework().setStubbingListener(reporter);
        try {
            // add listener that detects test failures
            notifier.addListener(listener);
            runner.run(notifier);
        } finally {
            Mockito.framework().setStubbingListener(null);
        }

        if (!filterRequested && listener.isSussessful()) {
            //only report when:
            //1. if all tests from given test have ran (filter requested is false)
            //   Otherwise we would report unnecessary stubs even if the user runs just single test from the class
            //2. tests are successful (we don't want to add an extra failure on top of any existing failure, to avoid confusion)
            //TODO JUnit runner should have a specific message explaining to the user how it works.
            reporter.report(testClass, notifier);
        }
    }

    public Description getDescription() {
        return runner.getDescription();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        filterRequested = true;
        runner.filter(filter);
    }
}

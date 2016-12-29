package org.mockitoutil;

/**
 * Clean asserts for exception handling
 */
public class ThrowableAssert {

    private Throwable reportedException;

    private ThrowableAssert(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            this.reportedException = t;
            return;
        }
        throw new AssertionError("Expected runnable to throw an exception but it didn't");
    }
    public void throwsException(Class<? extends Throwable> exceptionType) {
        if(!exceptionType.isInstance(reportedException)) {
            throw new AssertionError("Expect the exception to be of type: "
                    + exceptionType.getSimpleName() + " but it was: "
                    + reportedException.getClass().getSimpleName());
        }
    }

    /**
     * Executes provided runnable, expects it to throw an exception.
     * Then, it offers ways to assert on the expected exception.
     */
    public static ThrowableAssert assertThat(Runnable runnable) {
        return new ThrowableAssert(runnable);
    }
}

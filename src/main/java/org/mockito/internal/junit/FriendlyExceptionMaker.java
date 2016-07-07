package org.mockito.internal.junit;

import org.mockito.exceptions.verification.ArgumentsAreDifferent;

/**
 * If JUnit is used, we can use an exception that extends from ComparisonFailure
 * and hence provide a better IDE support as the comparison result is comparable
 */
class FriendlyExceptionMaker {

    private final JUnitDetecter detecter;

    FriendlyExceptionMaker(JUnitDetecter detecter) {
        this.detecter = detecter;
    }

    //TODO SF this can be now unit tested
    public AssertionError createArgumentsAreDifferentException(String message, String wanted, String actual)  {
        if (!detecter.hasJUnit()) {
            return new ArgumentsAreDifferent(message);
        }

        try {
            Class<?> clazz = Class.forName("org.mockito.exceptions.verification.junit.ArgumentsAreDifferent");
            return (AssertionError) clazz.getConstructors()[0].newInstance(message, wanted, actual);
        } catch (Throwable t) {
//            throw the default exception in case of problems
            return new ArgumentsAreDifferent(message);
        }
    }
}

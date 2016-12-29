package org.mockito;

/**
 * Allows to manage Mockito strictness (debugging, cleaner tests) without JUnit.
 * <p>
 * TODO javadoc
 */
@Incubating
public interface MockitoMocking {

    /**
     * Must be invoked after the test has completed
     */
    @Incubating
    void finishMocking();
}

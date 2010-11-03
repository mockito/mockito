package org.mockito.internal.configuration.injection;

/**
 * Allow the ongoing injection of a mock candidate.
 */
public interface OngoingInjecter {

    /**
     * Inject the mock.
     *
     * <p>
     * Please check the actual implementation.
     * </p>
     *
     * @return <code>true</code> if injected, <code>false</code> otherwise.
     */
    boolean thenInject();

}
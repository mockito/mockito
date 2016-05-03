/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

/**
 * Provides access to the {@link MockingProgress} of a corresponding {@link Thread}. Every {@link Thread} in Mockito has it s own {@link MockingProgress} to avoid data races while stubbing.
 */
public class ThreadSafeMockingProgress {

    private static final ThreadLocal<MockingProgress> MOCKING_PROGRESS_PROVIDER = new ThreadLocal<MockingProgress>() {
        @Override
        protected MockingProgress initialValue() {
            return new MockingProgressImpl();
        }
    };

    private ThreadSafeMockingProgress() {
    }

    /**
     * Returns the {@link MockingProgress} for the current Thread.
     * <p>
     * <b>IMPORTANT</b>: Never assign and access the returned {@link MockingProgress} to an instance or static field. Thread safety can not be guaranteed in this case, cause the Thread that wrote the field might not be the same that read it. In other words multiple threads will access the same {@link MockingProgress}.
     *
     * @return never <code>null</code>
     */
    public final static MockingProgress mockingProgress() {
        return MOCKING_PROGRESS_PROVIDER.get();
    }
}
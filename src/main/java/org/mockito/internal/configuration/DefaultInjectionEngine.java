/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.reflect.Field;
import java.util.Set;

import org.mockito.internal.configuration.injection.MockInjection;

/**
 * Inject mock/spies dependencies for fields annotated with &#064;InjectMocks
 * <p/>
 * See {@link org.mockito.MockitoAnnotations}
 */
public class DefaultInjectionEngine {

    /**
     * Proceeds to ongoing mocks injection on fields with:
     * <ul>
     * <li>strict constructor injection strategy to not allow semi-initialized fields at this step
     * <li>lenient field/property injection strategy to skip fields without no-args constructor
     * </ul>
     *
     * @param needingInjection  fields needing injection
     * @param mocks             mocks available for injection
     * @param testClassInstance instance of the test
     */
    public void injectOngoingMocksOnFields(
            Set<Field> needingInjection, Set<Object> mocks, Object testClassInstance) {
        MockInjection.onFields(needingInjection, testClassInstance)
                .withMocks(mocks)
                .tryStrictConstructorInjection()
                .tryLenientPropertyOrFieldInjection()
                .handleSpyAnnotation()
                .apply();
    }

    /**
     * Proceeds to terminal mocks injection on fields with:
     * <ul>
     * <li>lenient constructor injection strategy to initialize fields even with null arguments
     * <li>strict field/property injection strategy to fail on fields without no-args constructor
     * </ul>
     *
     * @param needingInjection fields needing injection
     * @param mocks mocks available for injection
     * @param testClassInstance instance of the test
     */
    public void injectMocksOnFields(
            Set<Field> needingInjection, Set<Object> mocks, Object testClassInstance) {
        MockInjection.onFields(needingInjection, testClassInstance)
                .withMocks(mocks)
                .tryConstructorInjection()
                .tryPropertyOrFieldInjection()
                .handleSpyAnnotation()
                .apply();
    }
}

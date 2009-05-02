/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import org.junit.internal.runners.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.mockito.*;
import org.mockito.internal.runners.*;


/**
 * <b>JUnit 4.4</b> runner initializes mocks annotated with {@link Mock},
 * so that explicit usage of {@link MockitoAnnotations#initMocks(Object)} is not necessary. 
 * Mocks are initialized before each test method. 
 * <p>
 * Runner is completely optional - there are other ways you can get &#064;Mock working, for example by writing a base class.
 * <p>
 * Read more in javadoc for {@link MockitoAnnotations}
 * <p>
 * Example:
 * <pre>
 * &#064;RunWith(MockitoJUnit44Runner.class)
 * public class ExampleTest {
 * 
 *     &#064;Mock
 *     private List list;
 * 
 *     &#064;Test
 *     public void shouldDoSomething() {
 *         list.add(100);
 *     }
 * }
 * <p>
 * 
 * </pre>
 */
@SuppressWarnings("deprecation")
public class MockitoJUnit44Runner extends Runner {

    private LegacyJUnitRunner legacyRunner;

    public MockitoJUnit44Runner(Class<?> klass) throws InitializationError {
        legacyRunner = new LegacyJUnitRunner(klass, new TestCreationListener() {
            public void testCreated(Object test) {
                MockitoAnnotations.initMocks(test);
            }
        });
    }

    @Override
    public void run(final RunNotifier notifier) {
        // add listener that validates framework usage at the end of each test
        notifier.addListener(new FrameworkUsageValidator(notifier));

        legacyRunner.run(notifier);
    }

    @Override
    public Description getDescription() {
        return legacyRunner.getDescription();
    }
}
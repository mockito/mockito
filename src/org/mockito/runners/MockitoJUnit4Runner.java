/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Mockito integration with JUnit 4.x.
 * <p>
 * If JUnit version 4.x (4.0-4.4) is in use {@link RunWith} annotation can be
 * used to enable custom Mockito runner. This runner automatically initializes
 * mocks annotated with {@link Mock}, so that explicit usage of
 * {@link MockitoAnnotations#initMocks(Object)} is not necessary. Mocks are
 * initialized before each invocation of particular test method.
 * <p>
 * Example of use in test class:
 * 
 * <pre>
 * &#064;RunWith(MockitoJUnit4Runner.class)
 * public class ExampleTest {
 * 
 *     &#064;Mock
 *     private List list;
 * 
 *     &#064;Test
 *     public void shouldInitMocksUsingRunner() {
 *         list.add(&quot;test&quot;);
 *     }
 * }
 * </pre>
 */
public class MockitoJUnit4Runner extends JUnit4ClassRunner {

    public MockitoJUnit4Runner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        MockitoAnnotations.initMocks(test);
        return test;
    }

}
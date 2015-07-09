/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.runners;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;

/**
 * <b>Deprecated: Simply use {@link MockitoJUnitRunner}</b>
 * <p>
 * Compatible only with <b>JUnit 4.4</b>, this runner adds following behavior:
 * <ul>
 *   <li>
 *      Initializes mocks annotated with {@link Mock},
 *      so that explicit usage of {@link MockitoAnnotations#initMocks(Object)} is not necessary. 
 *      Mocks are initialized before each test method.
 *   <li>
 *      validates framework usage after each test method. See javadoc for {@link Mockito#validateMockitoUsage()}.
 * </ul>
 * 
 * Runner is completely optional - there are other ways you can get &#064;Mock working, for example by writing a base class.
 * Explicitly validating framework usage is also optional because it is triggered automatically by Mockito every time you use the framework.
 * See javadoc for {@link Mockito#validateMockitoUsage()}.
 * <p>
 * Read more about &#064;Mock annotation in javadoc for {@link MockitoAnnotations}
 * <p>
 * Example:
 * <pre class="code"><code class="java">
 * &#064;RunWith(MockitoJUnitRunner.class)
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
 * </code></pre>
 */
@Deprecated
public class MockitoJUnit44Runner extends MockitoJUnitRunner {

    public MockitoJUnit44Runner(Class<?> klass) throws InvocationTargetException {
        super(klass);
    }
}
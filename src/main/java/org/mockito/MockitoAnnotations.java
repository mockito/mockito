/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.plugins.AnnotationEngine;

import static org.mockito.internal.util.StringUtil.*;

/**
 * MockitoAnnotations.openMocks(this); initializes fields annotated with Mockito annotations.
 * See also {@link MockitoSession} which not only initializes mocks
 * but also adds extra validation for cleaner tests!
 * <p>
 * <ul>
 * <li>Allows shorthand creation of objects required for testing.</li>
 * <li>Minimizes repetitive mock creation code.</li>
 * <li>Makes the test class more readable.</li>
 * <li>Makes the verification error easier to read because <b>field name</b> is used to identify the mock.</li>
 * </ul>
 *
 * <pre class="code"><code class="java">
 *   public class ArticleManagerTest extends SampleBaseTestCase {
 *
 *       &#064;Mock private ArticleCalculator calculator;
 *       &#064;Mock private ArticleDatabase database;
 *       &#064;Mock private UserProvider userProvider;
 *
 *       private ArticleManager manager;
 *
 *       &#064;Before public void setup() {
 *           manager = new ArticleManager(userProvider, database, calculator);
 *       }
 *   }
 *
 *   public class SampleBaseTestCase {
 *
 *       private AutoCloseable closeable;
 *
 *       &#064;Before public void openMocks() {
 *           closeable = MockitoAnnotations.openMocks(this);
 *       }
 *
 *       &#064;After public void releaseMocks() throws Exception {
 *           closeable.close();
 *       }
 *   }
 * </code></pre>
 * <p>
 * Read also about other annotations &#064;{@link Spy}, &#064;{@link Captor}, &#064;{@link InjectMocks}
 * <p>
 * <b><code>MockitoAnnotations.openMocks(this)</code></b> method has to be called to initialize annotated fields.
 * <p>
 * In above example, <code>openMocks()</code> is called in &#064;Before (JUnit4) method of test's base class.
 * For JUnit3 <code>openMocks()</code> can go to <code>setup()</code> method of a base class.
 * You can also put openMocks() in your JUnit runner (&#064;RunWith) or use built-in runner: {@link MockitoJUnitRunner}.
 * If static method mocks are used, it is required to close the initialization. Additionally, if using third-party
 * mock-makers, other life-cycles might be handled by the open-release routine.
 */
public class MockitoAnnotations {

    /**
     * Initializes objects annotated with Mockito annotations for given testClass:
     *  &#064;{@link org.mockito.Mock}, &#064;{@link Spy}, &#064;{@link Captor}, &#064;{@link InjectMocks}
     * <p>
     * See examples in javadoc for {@link MockitoAnnotations} class.
     *
     * @return A closable to close when completing any tests in {@code testClass}.
     */
    public static AutoCloseable openMocks(Object testClass) {
        if (testClass == null) {
            throw new MockitoException(
                    "testClass cannot be null. For info how to use @Mock annotations see examples in javadoc for MockitoAnnotations class");
        }

        AnnotationEngine annotationEngine =
                new GlobalConfiguration().tryGetPluginAnnotationEngine();
        return annotationEngine.process(testClass.getClass(), testClass);
    }

    /**
     * Initializes objects annotated with Mockito annotations for given testClass:
     *  &#064;{@link org.mockito.Mock}, &#064;{@link Spy}, &#064;{@link Captor}, &#064;{@link InjectMocks}
     * <p>
     * See examples in javadoc for {@link MockitoAnnotations} class.
     *
     * @deprecated Use {@link MockitoAnnotations#openMocks(Object)} instead.
     * This method is equivalent to {@code openMocks(testClass).close()}.
     * The close method should however only be called after completed usage of {@code testClass}.
     * If using static-mocks or custom {@link org.mockito.plugins.MockMaker}s, using this method might
     * cause misbehavior of mocks injected into the test class.
     */
    @Deprecated
    public static void initMocks(Object testClass) {
        try {
            openMocks(testClass).close();
        } catch (Exception e) {
            throw new MockitoException(
                    join(
                            "Failed to release mocks",
                            "",
                            "This should not happen unless you are using a third-part mock maker"),
                    e);
        }
    }
}

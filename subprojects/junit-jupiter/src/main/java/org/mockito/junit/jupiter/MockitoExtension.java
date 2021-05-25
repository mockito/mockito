/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit.jupiter;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.internal.configuration.MockAnnotationProcessor;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.session.MockitoSessionLoggerAdapter;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

/**
 * Extension that initializes mocks and handles strict stubbings. This extension is the JUnit Jupiter equivalent
 * of our JUnit4 {@link MockitoJUnitRunner}.
 *
 * Example usage:
 *
 * <pre class="code"><code class="java">
 * <b>&#064;ExtendWith(MockitoExtension.class)</b>
 * public class ExampleTest {
 *
 *     &#064;Mock
 *     private List&lt;Integer&gt; list;
 *
 *     &#064;Test
 *     public void shouldDoSomething() {
 *         list.add(100);
 *     }
 * }
 * </code></pre>
 *
 * If you would like to configure the used strictness for the test class, use {@link MockitoSettings}.
 *
 * <pre class="code"><code class="java">
 * <b>&#064;MockitoSettings(strictness = Strictness.STRICT_STUBS)</b>
 * public class ExampleTest {
 *
 *     &#064;Mock
 *     private List&lt;Integer&gt; list;
 *
 *     &#064;Test
 *     public void shouldDoSomething() {
 *         list.add(100);
 *     }
 * }
 * </code></pre>
 *
 * This extension also supports JUnit Jupiter's method parameters.
 * Use parameters for initialization of mocks that you use only in that specific test method.
 * In other words, where you would initialize local mocks in JUnit 4 by calling {@link Mockito#mock(Class)},
 * use the method parameter. This is especially beneficial when initializing a mock with generics, as you no
 * longer get a warning about "Unchecked assignment".
 * Please refer to JUnit Jupiter's documentation to learn when method parameters are useful.
 *
 * <pre class="code"><code class="java">
 * <b>&#064;ExtendWith(MockitoExtension.class)</b>
 * public class ExampleTest {
 *
 *     &#064;Mock
 *     private List&lt;Integer&gt; sharedList;
 *
 *     &#064;Test
 *     public void shouldDoSomething() {
 *         sharedList.add(100);
 *     }
 *
 *     &#064;Test
 *     public void hasLocalMockInThisTest(@Mock List&lt;Integer&gt; localList) {
 *         localList.add(100);
 *         sharedList.add(100);
 *     }
 * }
 * </code></pre>
 *
 * Lastly, the extension supports JUnit Jupiter's constructor parameters.
 * This allows you to do setup work in the constructor and set
 * your fields to <code>final</code>.
 * Please refer to JUnit Jupiter's documentation to learn when constructor parameters are useful.
 *
 * <pre class="code"><code class="java">
 * <b>&#064;ExtendWith(MockitoExtension.class)</b>
 * public class ExampleTest {
 *
 *      private final List&lt;Integer&gt; sharedList;
 *
 *      ExampleTest(&#064;Mock sharedList) {
 *          this.sharedList = sharedList;
 *      }
 *
 *      &#064;Test
 *      public void shouldDoSomething() {
 *          sharedList.add(100);
 *      }
 * }
 * </code></pre>
 */
public class MockitoExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private final static Namespace MOCKITO = create("org.mockito");

    private final static String SESSION = "session", MOCKS = "mocks";

    private final Strictness strictness;

    // This constructor is invoked by JUnit Jupiter via reflection or ServiceLoader
    @SuppressWarnings("unused")
    public MockitoExtension() {
        this(Strictness.STRICT_STUBS);
    }

    private MockitoExtension(Strictness strictness) {
        this.strictness = strictness;
    }

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(final ExtensionContext context) {
        List<Object> testInstances = context.getRequiredTestInstances().getAllInstances();

        Strictness actualStrictness = this.retrieveAnnotationFromTestClasses(context)
            .map(MockitoSettings::strictness)
            .orElse(strictness);

        MockitoSession session = Mockito.mockitoSession()
            .initMocks(testInstances.toArray())
            .strictness(actualStrictness)
            .logger(new MockitoSessionLoggerAdapter(Plugins.getMockitoLogger()))
            .startMocking();

        context.getStore(MOCKITO).put(MOCKS, new HashSet<>());
        context.getStore(MOCKITO).put(SESSION, session);
    }

    private Optional<MockitoSettings> retrieveAnnotationFromTestClasses(final ExtensionContext context) {
        ExtensionContext currentContext = context;
        Optional<MockitoSettings> annotation;

        do {
            annotation = findAnnotation(currentContext.getElement(), MockitoSettings.class);

            if (!currentContext.getParent().isPresent()) {
                break;
            }

            currentContext = currentContext.getParent().get();
        } while (!annotation.isPresent() && currentContext != context.getRoot());

        return annotation;
    }

    /**
     * Callback that is invoked <em>after</em> each test has been invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void afterEach(ExtensionContext context) {
        context.getStore(MOCKITO).remove(MOCKS, Set.class).forEach(mock -> ((MockedStatic<?>) mock).closeOnDemand());
        context.getStore(MOCKITO).remove(SESSION, MockitoSession.class)
                .finishMocking(context.getExecutionException().orElse(null));
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
        return parameterContext.isAnnotated(Mock.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        Object mock = MockAnnotationProcessor.processAnnotationForMock(
            parameterContext.findAnnotation(Mock.class).get(),
            parameter.getType(),
            parameter::getParameterizedType,
            parameter.getName());
        if (mock instanceof MockedStatic<?>) {
            context.getStore(MOCKITO).get(MOCKS, Set.class).add(mock);
        }
        return mock;
    }
}

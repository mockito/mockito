/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit.jupiter;


import java.lang.reflect.Parameter;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.internal.configuration.MockAnnotationProcessor;
import org.mockito.internal.junit.StubbingCheckingCreationListener;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

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
public class MockitoExtension implements TestInstancePostProcessor, BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback, ParameterResolver {

    private final static Namespace MOCKITO = create("org.mockito");

    private final static String SESSION = "session";
    private final static String TEST_INSTANCE = "testInstance";
    private static final String MOCK_CREATED_LISTENER = "MockCreatedListener";

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
     * <p>Callback for post-processing the supplied test instance.
     *
     * <p><strong>Note</strong>: the {@code ExtensionContext} supplied to a
     * {@code TestInstancePostProcessor} will always return an empty
     * {@link Optional} value from {@link ExtensionContext#getTestInstance()
     * getTestInstance()}. A {@code TestInstancePostProcessor} should therefore
     * only attempt to process the supplied {@code testInstance}.
     *
     * @param testInstance the instance to post-process; never {@code null}
     * @param context      the current extension context; never {@code null}
     */
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context){
        context.getStore(MOCKITO).put(TEST_INSTANCE, testInstance);
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        final StubbingCheckingCreationListener mockCreatedListener = context.getStore(MOCKITO).getOrComputeIfAbsent(MOCK_CREATED_LISTENER, k ->
                                                                                                                    {
                                                                                                                        StubbingCheckingCreationListener creationListener = new StubbingCheckingCreationListener();
                                                                                                                        Mockito.framework().addListener(creationListener);
                                                                                                                        return creationListener;
                                                                                                                    },
                                                                                                                    StubbingCheckingCreationListener.class);
        Strictness actualStrictness = this.retrieveAnnotationFromTestClasses(context)
                                          .map(MockitoSettings::strictness)
                                          .orElse(strictness);

        mockCreatedListener.strictnessForTest(context.getDisplayName(), actualStrictness);
    }

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(final ExtensionContext context) {
        Set<Object> testInstances = new LinkedHashSet<>();
        testInstances.add(context.getRequiredTestInstance());

        this.collectParentTestInstances(context, testInstances);

        Strictness actualStrictness = this.retrieveAnnotationFromTestClasses(context)
            .map(MockitoSettings::strictness)
            .orElse(strictness);

        MockitoSession session = Mockito.mockitoSession()
            .initMocks(testInstances.toArray())
            .strictness(actualStrictness)
            .disableStubbingErrorReporting()
            .startMocking();

        context.getStore(MOCKITO).put(SESSION, session);

        final String displayName = context.getDisplayName();
        context.getStore(MOCKITO).get(MOCK_CREATED_LISTENER, StubbingCheckingCreationListener.class).strictnessForTest(displayName, actualStrictness);
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

    private void collectParentTestInstances(ExtensionContext context, Set<Object> testInstances) {
        Optional<ExtensionContext> parent = context.getParent();

        while (parent.isPresent() && parent.get() != context.getRoot()) {
            ExtensionContext parentContext = parent.get();

            Object testInstance = parentContext.getStore(MOCKITO).remove(TEST_INSTANCE);

            if (testInstance != null) {
                testInstances.add(testInstance);
            }

            parent = parentContext.getParent();
        }
    }

    /**
     * Callback that is invoked <em>after</em> each test has been invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterEach(ExtensionContext context) {
        context.getStore(MOCKITO).remove(SESSION, MockitoSession.class)
                .finishMocking();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        final ExtensionContext.Store mockitoStore = context.getStore(MOCKITO);
        final StubbingCheckingCreationListener mockCreatedListener = mockitoStore.get(MOCK_CREATED_LISTENER, StubbingCheckingCreationListener.class);
        try {
            if (mockCreatedListener != null) {
                mockCreatedListener.checkStubbing(context.getTestClass().orElse(null), context.getDisplayName());
            }
        } finally {
            //Remove does not check parent contexts so only remove only returns a value in the context which added it.
            //Finally to ensure it happens even if we found unused stubbings
            final StubbingCheckingCreationListener removedListener = mockitoStore.remove(MOCK_CREATED_LISTENER, StubbingCheckingCreationListener.class);
            if (removedListener != null) {
                Mockito.framework().removeListener(removedListener);
                removedListener.checkStubbing(context.getTestClass().orElse(null));
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(Mock.class);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        return MockAnnotationProcessor.processAnnotationForMock(parameterContext.findAnnotation(Mock.class).get(), parameter.getType(), parameter.getName());
    }
}

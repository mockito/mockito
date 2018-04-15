/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit.jupiter;


import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
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
 *     private List list;
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
 *     private List list;
 *
 *     &#064;Test
 *     public void shouldDoSomething() {
 *         list.add(100);
 *     }
 * }
 * </code></pre>
 */
public class MockitoExtension implements TestInstancePostProcessor,BeforeEachCallback, AfterEachCallback {

    private final static Namespace MOCKITO = create("org.mockito");

    private final static String SESSION = "session";
    private final static String TEST_INSTANCE = "testInstance";

    private final Strictness strictness;

    // This constructor is invoked by JUnit Jupiter via reflection
    @SuppressWarnings("unused")
    private MockitoExtension() {
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
            .startMocking();

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

}

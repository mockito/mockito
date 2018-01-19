/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit5;


import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

public class MockitoExtension implements TestInstancePostProcessor,BeforeEachCallback, AfterEachCallback {

    private final static Namespace MOCKITO = create("org.mockito");

    private final static String SESSION = "session";
    private final static String TEST_INSTANCE = "testInstance";

    private final Strictness strictness;

    private MockitoExtension() {
        this(Strictness.WARN);
    }

    private MockitoExtension(Strictness strictness) {
        this.strictness = strictness;
    }

    /**
     * Callback for post-processing the supplied test instance.
     * <p>
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
        context.getStore(MOCKITO).put(TEST_INSTANCE,testInstance);
    }

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(final ExtensionContext context) {
        List<Object> testInstances = new LinkedList<>();
        testInstances.add(context.getRequiredTestInstance());

        collectParentTestInstances(context, testInstances);

        Strictness actualStrictness = strictness;

        if (context.getElement().isPresent()) {
            final Optional<org.mockito.junit5.Strictness> annotation = findAnnotation(context.getElement().get(), org.mockito.junit5.Strictness.class);

            if (annotation.isPresent()) {
                actualStrictness = annotation.get().value();
            }
        }

        MockitoSession session = Mockito.mockitoSession()
            .initTestInstances(testInstances)
            .strictness(actualStrictness)
            .startMocking();

        store(context, session);
    }

    private void collectParentTestInstances(ExtensionContext context, List<Object> testInstances) {
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

    private static void store(ExtensionContext context, MockitoSession session) {
        context.getStore(MOCKITO).put(SESSION, session);
    }

    private static MockitoSession removeMockitoSession(ExtensionContext context) {
        return context.getStore(MOCKITO).remove(SESSION, MockitoSession.class);
    }

    /**
     * Callback that is invoked <em>after</em> each test has been invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterEach(ExtensionContext context) {
        removeMockitoSession(context)
                .finishMocking();
    }

    public static class Lenient extends MockitoExtension {
        private Lenient() {
            super(Strictness.LENIENT);
        }
    }

    public static class Strict extends MockitoExtension {
        private Strict() {
            super(Strictness.STRICT_STUBS);
        }
    }
}

package org.mockito.junit5;


import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.mockito.Mockito;
import org.mockito.MockitoSession;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;
import static org.mockito.quality.Strictness.WARN;

public class MockitoExtension implements BeforeEachCallback, AfterEachCallback {

    private final static Namespace MOCKITO = create("org.mockito");

    private final static String SESSION = "session";

    /**
     * FIXME we should use a more common place in mockito (MockitoCore?) to define the default strictness,
     * it should  be uses by Runner & Rule too
     */
    private final static org.mockito.quality.Strictness DEFAULT_STRICTNESS = WARN;

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        Object testInstance = context.getRequiredTestInstance();

        org.mockito.quality.Strictness strictness = retrieveStrictness(context);

        MockitoSession session = Mockito.mockitoSession()
            .initMocks(testInstance)
            .strictness(strictness)
            .startMocking();

        store(context, session);
    }


    private static org.mockito.quality.Strictness retrieveStrictness(ExtensionContext context) {


        Optional<AnnotatedElement> annotatedElement = context.getElement();
        if (!annotatedElement.isPresent()) {
            return DEFAULT_STRICTNESS;
        }

        AnnotatedElement methodOrClass = annotatedElement.get();


        Optional<Strictness> annotation = findAnnotation(methodOrClass, Strictness.class);

        if (annotation.isPresent()) {
            org.mockito.quality.Strictness strictness = annotation.get().value();
            return strictness;
        }

        Optional<ExtensionContext> parent = context.getParent();

        if (parent.isPresent()) {
            return retrieveStrictness(parent.get());
        }

        return DEFAULT_STRICTNESS;


    }

    private static void store(ExtensionContext context, MockitoSession session) {
        context.getStore(MOCKITO).put("session", session);
    }

    private static MockitoSession removeMockitoSession(ExtensionContext context) {
        return context.getStore(MOCKITO).remove(SESSION,MockitoSession.class);
    }

    /**
     * Callback that is invoked <em>after</em> each test has been invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterEach(ExtensionContext context) {
        MockitoSession session;
        session = removeMockitoSession(context);
        session.finishMocking();
    }
}


package org.mockito.junit5;


import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.quality.Strictness.WARN;

public class MockitoExtension implements BeforeEachCallback, AfterEachCallback {

    private MockitoSession session;

    private final static Strictness DEFAULT_STRICTNESS = WARN;

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        Strictness strictness = retrieveStrictness(context);

        Object testInstance = context.getRequiredTestInstance();

        session = mockitoSession()
            .initMocks(testInstance)
            .strictness(strictness)
            .startMocking();

    }

    private static Strictness retrieveStrictness(ExtensionContext context) {
        Optional<AnnotatedElement> annotatedElement = context.getElement();
        if (!annotatedElement.isPresent()) {
            return DEFAULT_STRICTNESS;
        }

        AnnotatedElement methodOrClass = annotatedElement.get();
        org.mockito.junit5.Strictness annotation = methodOrClass.getAnnotation(org.mockito.junit5.Strictness.class);

        if (annotation == null ) {
            Optional<ExtensionContext> parent = context.getParent();

            if (parent.isPresent()){
                return retrieveStrictness(parent.get());
            }

            return DEFAULT_STRICTNESS;
        }


        Strictness strictness = annotation.value();
        if (strictness == null) {
            return DEFAULT_STRICTNESS;
        }

        return strictness;
    }

    /**
     * Callback that is invoked <em>after</em> each test has been invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterEach(ExtensionContext context) {
        session.finishMocking();
    }
}

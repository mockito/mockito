package org.mockito.internal.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.junit5.Strictness;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;
import static org.mockito.quality.Strictness.WARN;

public class AnnotationUtil {
    /**
     * FIXME we should use a more common place in mockito (MockitoCore?) to define the default strictness,
     * it should  be uses by Runner & Rule too
     */
    private final static org.mockito.quality.Strictness DEFAULT_STRICTNESS = WARN;

    public static org.mockito.quality.Strictness retrieveStrictness(ExtensionContext context) {
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
}

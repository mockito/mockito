package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;

import org.mockito.DoNotMock;
import org.mockito.plugins.DoNotMockEnforcer;

/**
 * Default implementation that enforces @DoNotMock-style annotations.
 *
 * This version fixes the behavior for custom annotations whose FQN ends with
 * "org.mockito.DoNotMock": the violation message now includes the annotation's
 * toString(), so any fields (e.g., reason) are visible in the output.
 */
public final class DefaultDoNotMockEnforcer implements DoNotMockEnforcer {

    @Override
    public String checkTypeForDoNotMockViolation(Class<?> type) {
        for (Annotation annotation : type.getAnnotations()) {
            Class<? extends Annotation> at = annotation.annotationType();
            String fqn = at.getName();

            // (A) Official annotation: org.mockito.DoNotMock
            if (DoNotMock.class.getName().equals(fqn)) {
                DoNotMock dnm = (DoNotMock) annotation;
                String reason = dnm.reason();
                String message = type.getName()
                    + " is annotated with @" + DoNotMock.class.getName()
                    + " and can't be mocked.";
                if (reason != null && !reason.isEmpty()) {
                    message += " Reason: " + reason;
                }
                return message;
            }

            // (B) Custom annotation: FQN ends with "org.mockito.DoNotMock"
            // Key fix: include annotation.toString() so fields (e.g., reason) appear in the message.
            if (fqn.endsWith("org.mockito.DoNotMock")) {
                return type.getName()
                    + " is annotated with @" + annotation
                    + " and can't be mocked.";
            }
        }
        return null;
    }
}

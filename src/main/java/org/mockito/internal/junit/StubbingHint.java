package org.mockito.internal.junit;

/**
 * Stubbing hint emitted to the user
 */
class StubbingHint {

    private final StringBuilder hint;

    StubbingHint() {
        hint = new StringBuilder("[MockitoHint] See javadoc for MockitoHint class.");
    }

    void appendLine(Object ... elements) {
        hint.append("\n[MockitoHint] ");
        for (Object e : elements) {
            hint.append(e);
        }
    }

    public String toString() {
        return hint.toString();
    }
}

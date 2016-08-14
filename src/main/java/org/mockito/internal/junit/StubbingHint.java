package org.mockito.internal.junit;

/**
 * Stubbing hint emitted to the user
 */
class StubbingHint {

    private final StringBuilder hint;

    StubbingHint(String testName) {
        hint = new StringBuilder("[MockitoHint] ")
            .append(testName).append(" (see javadoc for MockitoHint):");
    }

    void appendLine(Object ... elements) {
        hint.append("\n[MockitoHint] ");
        for (Object e : elements) {
            hint.append(e);
        }
    }

    public String toString() {
        return hint.toString() + "\n";
    }
}

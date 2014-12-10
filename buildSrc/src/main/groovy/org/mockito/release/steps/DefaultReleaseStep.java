package org.mockito.release.steps;

import org.mockito.release.util.operations.Operation;

class DefaultReleaseStep implements ReleaseStep {

    private final String description;
    private final Operation operation;

    public DefaultReleaseStep(String description, Operation operation) {
        this.description = description;
        this.operation = operation;
    }

    public String getDescription() {
        return description;
    }

    public void perform() {
        operation.perform();
    }
}

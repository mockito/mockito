package org.mockito.release.steps;

import groovy.lang.Closure;
import org.mockito.release.util.operations.Operation;
import org.mockito.release.util.operations.Operations;

class DefaultReleaseStep implements ReleaseStep {

    private final String description;
    private final Operation operation;
    private Operation rollback;

    public DefaultReleaseStep(String description, Operation operation) {
        this.description = description;
        this.operation = operation;
    }

    public String getDescription() {
        return description;
    }

    public void rollback(Closure closure) {
        rollback = Operations.toOperation(closure);
    }

    public Operation getRollback() {
        return rollback;
    }

    public void perform() {
        operation.perform();
    }
}

package org.mockito.release.steps;

import groovy.lang.Closure;
import org.mockito.release.util.operations.Operation;
import org.mockito.release.util.operations.Operations;

class DefaultReleaseStep implements ConfigurableReleaseStep {

  private final String description;
  private final Operation operation;
  private Operation rollback;
  private Operation cleanup;

  public DefaultReleaseStep(String description, Operation operation) {
    this.description = description;
    this.operation = operation;
  }

  public String getDescription() {
    return description;
  }

  public void performRollback() {
    if (rollback != null) {
      rollback.perform();
    } else if (cleanup != null) {
      cleanup.perform();
    } else {
      System.out.println("No rollback or cleanup operation found for step '" + description + "'");
    }
  }

  public void performCleanup() {
    if (cleanup != null) {
      System.out.println("Cleaning up after step '" + getDescription() + "'");
      cleanup.perform();
    } else {
      System.out.println("No cleanup found for step '" + getDescription() + "'");
    }
  }

  public void rollback(Closure closure) {
    rollback = Operations.toOperation(closure);
  }

  public void cleanup(Closure closure) {
    this.cleanup = Operations.toOperation(closure);
  }

  public void perform() {
    operation.perform();
  }
}

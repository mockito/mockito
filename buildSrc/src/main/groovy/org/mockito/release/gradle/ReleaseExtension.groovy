package org.mockito.release.gradle

import org.mockito.release.steps.ReleaseStep
import org.mockito.release.steps.ReleaseSteps
import org.mockito.release.util.operations.Operations

class ReleaseExtension {

  private final ReleaseSteps steps

  ReleaseExtension(ReleaseSteps steps) {
    this.steps = steps
  }

  ReleaseStep step(String description, Closure operation) {
    steps.newStep(description, Operations.toOperation(operation))
  }
}

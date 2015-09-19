package org.mockito.release.gradle.steps

import org.mockito.release.git.GitTool
import org.mockito.release.steps.ReleaseStep
import org.mockito.release.steps.ReleaseSteps
import org.mockito.release.util.operations.Operations

class ReleaseExtension {

  private final ReleaseSteps steps
  private final GitTool gitTool

  ReleaseExtension(ReleaseSteps steps, GitTool gitTool) {
    this.gitTool = gitTool
    this.steps = steps
  }

  ReleaseStep step(String description, Closure operation) {
    steps.newStep(description, Operations.toOperation(operation))
  }

  GitTool getGitTool() {
    gitTool
  }
}

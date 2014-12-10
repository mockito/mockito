package org.mockito.release.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.mockito.release.steps.ReleaseStep
import org.mockito.release.steps.ReleaseSteps
import org.mockito.release.steps.Steps
import org.mockito.release.util.operations.Operations

class ReleaseTask extends DefaultTask {

    ReleaseSteps steps = Steps.newSteps()

    ReleaseStep step(String description, Closure operation) {
        steps.newStep(description, Operations.toOperation(operation))
    }

    @TaskAction
    void release() {
        steps.perform();
    }
}
package org.mockito.release.gradle.steps

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.mockito.release.steps.ReleaseSteps

class ReleaseTask extends DefaultTask {

    ReleaseSteps steps

    @TaskAction
    void release() {
        steps.perform();
    }
}
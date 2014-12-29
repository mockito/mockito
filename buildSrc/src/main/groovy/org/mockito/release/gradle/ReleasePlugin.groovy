package org.mockito.release.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.mockito.release.steps.Steps

class ReleasePlugin implements Plugin<Project> {
  void apply(Project project) {
    def steps = Steps.newSteps()
    project.extensions.create("releaseSteps", ReleaseExtension, steps)
    def task = project.tasks.create("release", ReleaseTask)
    task.steps = steps

    project.tasks.addRule("releaseStep<Number> - performs given release step") { String taskName ->
      if (taskName.startsWith("releaseStep")) {
        String number = taskName - "releaseStep"
        def step = steps.getStep(number as int)
        def stepTask = project.tasks.create(taskName)
        stepTask.doLast { step.perform() }
      }
    }
  }
}

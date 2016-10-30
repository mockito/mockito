package org.mockito.workflow.gradle.internal;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.tasks.TaskState;

public class ReleaseWorkflowPlugin implements Plugin<Project> {
    public void apply(final Project project) {
        //main release task
        project.getTasks().create("release");
        final ReleaseWorkflowExtension ext = project.getExtensions().create("releaseWorkflow", ReleaseWorkflowExtension.class, project);

        //setup listener, so that the rollbacks are only executed if one of the main tasks fail
        project.getGradle().addListener(new TaskExecutionListener() {
            public void beforeExecute(Task task) {}
            public void afterExecute(Task task, TaskState taskState) {
                //when one of the main step tasks fails, enable all rollback tasks
                if (taskState.getFailure() != null && ext.steps.contains(task)) {
                    for (Task rollback : ext.rollbacks) {
                        rollback.setEnabled(true);
                    }
                }
            }
        });
    }

}

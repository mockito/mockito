package org.mockito.workflow.gradle.internal;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.TaskState;

public class ReleaseWorkflowPlugin implements Plugin<Project> {
    public void apply(final Project project) {
        final ReleaseWorkflowExtension ext = project.getExtensions().create("releaseWorkflow", ReleaseWorkflowExtension.class, project);
        project.afterEvaluate(new Action<Project>() {
            public void execute(Project project) {
                workflow(project, ext);
            }
        });
    }

    private static void workflow(Project project, final ReleaseWorkflowExtension ext) {
        //rollbacks only run their main task did not fail
        if (!project.hasProperty("dryRun")) { //accommodate testing
            for (int i = 0; i < ext.rollbacks.size(); i++) {
                final Task step = ext.steps.get(i);
                ext.rollbacks.get(i).onlyIf(new Spec<Task>() {
                    public boolean isSatisfiedBy(Task task) {
                        return step.getState().getFailure() == null;
                    }
                });
            }
        }

        //release steps must be sequential
        for (int i = 1; i < ext.steps.size(); i++) {
            ext.steps.get(i).dependsOn(ext.steps.get(i-1));
        }

        //rollbacks must run after all tasks, if they are scheduled
        for (Task rollback : ext.rollbacks) {
            rollback.mustRunAfter(ext.steps);
        }

        //rollbacks need to have order between themselves
        for (int i = 0; i < ext.rollbacks.size() - 1; i++) {
            ext.rollbacks.get(i).mustRunAfter(ext.rollbacks.get(i+1));
        }

        //setup listener, so that the rollbacks are only executed if one of the main tasks fail
        project.getGradle().addListener(new TaskExecutionListener() {
            public void beforeExecute(Task task) {}

            public void afterExecute(Task task, TaskState taskState) {
                //when main task fails, enable all rollback tasks
                if (taskState.getFailure() != null && ext.steps.contains(task)) {
                    for (Task rollback : ext.rollbacks) {
                        rollback.setEnabled(true);
                    }
                }
            }
        });

        Task release = project.getTasks().create("release");
        if (!ext.steps.isEmpty()) {
            //depend on last release step
            release.dependsOn(ext.steps.get(ext.steps.size() - 1));
        }
    }
}

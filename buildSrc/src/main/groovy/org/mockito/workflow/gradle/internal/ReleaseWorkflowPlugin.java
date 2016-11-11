package org.mockito.workflow.gradle.internal;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.tasks.TaskState;
import org.mockito.release.exec.Exec;
import org.mockito.release.git.Git;
import org.mockito.release.git.GitTool;

public class ReleaseWorkflowPlugin implements Plugin<Project> {
    public void apply(final Project project) {
        //main release task
        project.getTasks().create("release");
        final ReleaseWorkflowExtension ext = project.getExtensions().create("releaseWorkflow", ReleaseWorkflowExtension.class, project);

        //setup listener, so that the postSteps are only executed if one of the main tasks fail
        project.getGradle().addListener(new PostStepTaskEnabler(ext));

        //TODO very implicit, it needs to go to some tools for release, for example, "releaseTools.git"
        GitTool gitTool = Git.gitTool(Exec.getProcessRunner(project.getProjectDir()));
        project.getExtensions().getExtraProperties().set("gitTool", gitTool);
    }

    private static class PostStepTaskEnabler implements TaskExecutionListener {
        private final ReleaseWorkflowExtension ext;

        PostStepTaskEnabler(ReleaseWorkflowExtension ext) {
            this.ext = ext;
        }

        public void beforeExecute(Task task) {}

        public void afterExecute(Task task, TaskState taskState) {
            //when one of the main step tasks fails, enable all rollback tasks
            if (taskState.getFailure() != null && ext.steps.contains(task)) {
                for (Task rollback : ext.postSteps) {
                    rollback.setEnabled(true);
                }
            }
        }
    }
}

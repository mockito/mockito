package org.mockito.workflow.gradle.internal;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;
import org.mockito.workflow.gradle.ReleaseWorkflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ReleaseWorkflowExtension implements ReleaseWorkflow {

    final List<Task> steps = new ArrayList<Task>();
    Task previousStep;
    Task previousRollback;
    final List<Task> rollbacks = new ArrayList<Task>();
    private final Project project;
    private final List<Callable<Boolean>> allowers = new ArrayList<Callable<Boolean>>();

    public ReleaseWorkflowExtension(Project project) {
        this.project = project;
    }

    public ReleaseWorkflowExtension step(Task task, Map<String, Task> config) {
        addStep(task, config);
        return this;
    }

    public ReleaseWorkflowExtension step(Task task) {
        return step(task, Collections.<String, Task>emptyMap());
    }

    public ReleaseWorkflowExtension onlyIf(Callable<Boolean> predicate) {
        allowers.add(predicate);
        return this;
    }

    private void addStep(final Task task, Map<String, Task> config) {
        //populate steps collection
        steps.add(task);

        //main release task will depend on this step
        project.getTasks().getByName("release").dependsOn(task);

        //release steps must be sequential
        if (previousStep != null) {
            if (project.hasProperty("singleStep")) {
                //special case, release steps do not form any sequence
                //allowing the user to decide on the sequence from command line
            } else {
                //default mode, release steps form a sequence using 'dependsOn'
                //forcing execution of previous steps
                task.dependsOn(previousStep);
            }
        }
        previousStep = task;

        //only run task if it was allowed
        for (final Callable<Boolean> allower : allowers) {
            task.onlyIf(new SpecAdapter(allower));
        }

        StepConfiguration stepConfig = new StepConfiguration(config);

        if (stepConfig.isEmpty()) {
            return; //no rollback/cleanup configured
        }

        Task rollback = stepConfig.getRollback();
        if (rollback != null) {
            //rollbacks only run when one of the steps fails, by default we assume they don't fail
            if (!project.hasProperty("dryRun")) { //accommodate testing
                rollback.setEnabled(false);
            }
        } else {
            rollback = stepConfig.getCleanup();
            //cleanups run even if the release is successful
        }

        //populate main rollbacks list
        rollbacks.add(rollback);

        //rollback must run after every main task
        rollback.mustRunAfter(steps);

        //rollbacks need to have order between themselves
        if (previousRollback != null) {
            previousRollback.mustRunAfter(rollback);
        }
        previousRollback = rollback;

        //rollbacks finalize release steps
        task.finalizedBy(rollback);

        //rollbacks only run when their main task did not fail
        // when main task fails, there is nothing to rollback
        rollback.onlyIf(new Spec<Task>() {
            public boolean isSatisfiedBy(Task t) {
                return task.getState().getFailure() == null;
            }
        });

        //only run rollback if it is allowed
        for (Callable<Boolean> allower : allowers) {
            rollback.onlyIf(new SpecAdapter(allower));
        }
    }

    private static class SpecAdapter implements Spec<Task> {
        private final Callable<Boolean> allower;

        SpecAdapter(Callable<Boolean> allower) {
            this.allower = allower;
        }

        public boolean isSatisfiedBy(Task task) {
            try {
                return allower.call();
            } catch (Throwable t) {
                throw new RuntimeException("Unhandled exception thrown when release workflow" +
                        " attempted to evaluate whether to allow execution of task '" + task.getPath() + "' ", t);
            }
        }
    }
}

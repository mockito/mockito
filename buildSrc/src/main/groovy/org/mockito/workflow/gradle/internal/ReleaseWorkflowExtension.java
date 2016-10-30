package org.mockito.workflow.gradle.internal;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.mockito.workflow.gradle.ReleaseWorkflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReleaseWorkflowExtension implements ReleaseWorkflow {

    final List<Task> steps = new ArrayList<Task>();
    final List<Task> rollbacks = new ArrayList<Task>();
    private final Project project;

    public ReleaseWorkflowExtension(Project project) {
        this.project = project;
    }

    public ReleaseWorkflowExtension step(Task task, Map<String, Task> config) {
        addStep(task, config.get("rollback"));
        return this;
    }

    public ReleaseWorkflowExtension step(Task task) {
        return step(task, Collections.<String, Task>emptyMap());
    }

    private void addStep(Task task, Task rollback) {
        steps.add(task);
        if (rollback == null) {
            rollback = project.task("noopRollback" + capitalize(task.getName()));
        }
        rollbacks.add(rollback);
    }

    private static String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

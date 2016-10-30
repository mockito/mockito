package org.mockito.workflow.gradle;

import org.gradle.api.Task;
import org.mockito.workflow.gradle.internal.ReleaseWorkflowExtension;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Enables configuring release workflow steps
 */
public interface ReleaseWorkflow {

    /**
     * Adds release workflow step
     * @param task
     * @param config
     * @return
     */
    ReleaseWorkflowExtension step(Task task, Map<String, Task> config);

    ReleaseWorkflowExtension step(Task task);

    ReleaseWorkflowExtension onlyIf(Callable<Boolean> predicate);
}

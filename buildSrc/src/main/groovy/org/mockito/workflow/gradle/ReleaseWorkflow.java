package org.mockito.workflow.gradle;

import org.gradle.api.Task;
import org.mockito.workflow.gradle.internal.ReleaseWorkflowExtension;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Enables configuring release workflow steps.
 *
 * <h1>Example</h1>
 *
 * Example release workflow in build.gradle, outlining all functionality:
 *
 * <pre>
 * //Assuming that tasks 'checkReleaseNeeded', 'configureGenericGitUser' are already configured:
 *
 * releaseWorkflow {
 *     //step 1 of the release, invoke task that checks if release is needed
 *     step checkReleaseNeeded
 *
 *     //step 2, abort release based on a predicate
 *     onlyIf { checkReleaseNeeded.releaseNeeded }
 *
 *     //step 3, start using generic git user, register cleanup task
 *     step configureGenericGitUser, [cleanup: 'restoreOriginalGitUser']
 *
 *     //step 4, update release notes
 *     step updateReleaseNotes
 *
 *     //step 5, commit release notes, register a rollback task
 *     step commitReleaseNotes, [rollback: rollbackCommitReleaseNotes]
 *
 *     //step 6,
 *     step pushChanges
 * }
 * </pre>
 *
 * <h1>Workflow</h1>
 *
 * Explanation of the release workflow:
 *
 * <pre>
 * Release steps:
 *  - executed in same order as they are declared
 *  - if one of the steps fails, further steps are not executed
 *  - onlyIf {} predicate can be used to cleanly abort the release process based on predicate
 *
 * Rollback tasks:
 *  - executed only when one the release tasks fails, otherwise they are skipped
 *  - executed in reverse order
 *  - even if one rollback fails other rollback tasks are still executed
 *  - rollback of task 'foo' is _not_ executed if task 'foo' fails. It is only executed if any further release step fails.
 *   The reason for that is that when 'foo' fails, there is nothing to rollback.
 *
 * Cleanup tasks:
 *  - similar to rollback with one difference - it is executed even if the entire release is successful.
 *   Rollback tasks are only executed when one of the release steps fails.
 * </pre>
 *
 * <h1>Testing</h1>
 *
 * <pre>
 * Following project properties are supported:
 *  - 'dryRun' - when this project property is present, rollbacks are executed even if the release is successful.
 *   This is useful for testing and ensuring the state is cleanup afterwards.
 *   Normally, rollback tasks are only executed when one of the release steps fail.
 *  - 'singleStep' - when present, you can run single release step, without triggering prior steps from execution.
 *   Very useful for invoking single release steps for testing.
 *   You can specify multiple step tasks from command line and use this property.
 *  - 'dryRun' + 'singleStep' - using both properties at the same time is supported.
 * </pre>
 *
 */
public interface ReleaseWorkflow {

    /**
     * Adds given task to the release workflow.
     *
     * @param task - the task to add.
     * @param config - configuration of the task, map with exactly one key, either 'rollback' or 'cleanup'.
     */
    ReleaseWorkflowExtension step(Task task, Map<String, Task> config);

    /**
     * Adds task to release workflow, without any special configuration (rollback or cleanup)
     */
    ReleaseWorkflowExtension step(Task task);

    /**
     * Adds a predicate check that can cleanly abort the release process
     */
    ReleaseWorkflowExtension onlyIf(Callable<Boolean> predicate);
}

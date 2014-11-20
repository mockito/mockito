package org.mockito.release.notes.vcs;

import org.mockito.release.notes.exec.ProcessRunner;

/**
 * Vcs services
 */
public class Vcs {

    /**
     * Provides the ContributionsProvider
     */
    public static ContributionsProvider getContributionsProvider(ProcessRunner runner) {
        return new GitContributionsProvider(new GitLogProvider(runner));
    }
}

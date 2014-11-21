package org.mockito.release.notes.vcs;

import org.mockito.release.notes.exec.ProcessRunner;

/**
 * Vcs services
 */
public class Vcs {

    /**
     * Provides means to get contributions.
     */
    public static ContributionsProvider getGitProvider(ProcessRunner runner) {
        return new GitContributionsProvider(new GitLogProvider(runner), new IgnoreCiSkip());
    }
}

package org.mockito.release.notes.improvements;

/**
 * Improvements based on some issue tracking system outside of the vcs.
 */
public class Improvements {

    /**
     * Fetches tickets from GitHub. Needs GitHub auth token.
     *
     * @param authToken the GitHub auth token
     */
    public static ImprovementsProvider getGitHubProvider(final String authToken) {
        return new GitHubImprovementsProvider(authToken);
    }
}

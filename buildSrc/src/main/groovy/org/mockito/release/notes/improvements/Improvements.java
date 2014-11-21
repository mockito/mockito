package org.mockito.release.notes.improvements;

/**
 * Improvements based on some issue tracking system outside of the vcs.
 */
public class Improvements {

    /**
     * Fetches tickets from GitHub. Needs GitHub auth token.
     *
     * @param authTokenEnvVarName name of the env var that holds the token
     */
    public static ImprovementsProvider getGitHubProvider(final String authTokenEnvVarName) {
        return new GitHubImprovementsProvider(new GitHubAuthToken(authTokenEnvVarName));
    }
}

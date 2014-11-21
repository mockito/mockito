package org.mockito.release.notes.improvements;

import org.mockito.release.notes.util.ReleaseNotesException;

class GitHubAuthToken {

    private final String envVariableName;

    public GitHubAuthToken(String envVariableName) {
        this.envVariableName = envVariableName;
    }

    public String getToken() {
        String out = System.getenv(envVariableName);
        if (out == null) {
            throw new ReleaseNotesException("Environmental variable '" + envVariableName + "' is missing.", null);
        }
        return out;
    }
}

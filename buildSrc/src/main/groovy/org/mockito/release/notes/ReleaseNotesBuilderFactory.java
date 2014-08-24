package org.mockito.release.notes;

import org.gradle.api.Project;
import org.mockito.release.notes.internal.DefaultReleaseNotesBuilder;

class ReleaseNotesBuilderFactory {

    private final Project project;
    private String gitHubToken;
    private String ignorePattern;

    ReleaseNotesBuilderFactory(Project project) {
        this.project = project;
    }

    ReleaseNotesBuilder createBuilder() {
        return new DefaultReleaseNotesBuilder(project, gitHubToken, ignorePattern);
    }

    //TODO SF interface
    ReleaseNotesBuilderFactory gitHubToken(String gitHubToken) {
        this.gitHubToken = gitHubToken;
        return this;
    }

    ReleaseNotesBuilderFactory ignoreImprovementsMatching(String pattern) {
        this.ignorePattern = pattern;
        return this;
    }
}
package org.mockito.release.notes;

import org.gradle.api.Project;
import org.mockito.release.notes.internal.DefaultReleaseNotesBuilder;

class ReleaseNotesBuilderFactory {
    ReleaseNotesBuilder createBuilder(Project project, String gitHubToken) {
        return new DefaultReleaseNotesBuilder(project, gitHubToken);
    }
}
package org.mockito.release.notes;

import org.gradle.api.Project;
import org.mockito.release.notes.exec.Exec;
import org.mockito.release.notes.improvements.ImprovementSet;
import org.mockito.release.notes.improvements.ImprovementsProvider;
import org.mockito.release.notes.improvements.Improvements;
import org.mockito.release.notes.vcs.ContributionSet;
import org.mockito.release.notes.vcs.ContributionsProvider;
import org.mockito.release.notes.vcs.Vcs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

class GitNotesBuilder implements NotesBuilder {

    private static Logger LOG = LoggerFactory.getLogger(GitNotesBuilder.class);

    private final Project project;
    private final String authTokenEnvVar;

    GitNotesBuilder(Project project, String authTokenEnvVar) {
        this.authTokenEnvVar = authTokenEnvVar;
        this.project = project;
    }

    public String buildNotes(String fromRevision, String toRevision) {
        LOG.info("Getting release notes between {} and {}", fromRevision, toRevision);

        ContributionsProvider contributionsProvider = Vcs.getGitProvider(Exec.getGradleProcessRunner(project));
        ContributionSet contributions = contributionsProvider.getContributionsBetween(fromRevision, toRevision);

        ImprovementsProvider improvementsProvider = Improvements.getGitHubProvider(authTokenEnvVar);
        ImprovementSet improvements = improvementsProvider.getImprovements(contributions);

        return new NotesPrinter().printNotes(project.getVersion().toString(), new Date(), contributions, improvements);
    }
}

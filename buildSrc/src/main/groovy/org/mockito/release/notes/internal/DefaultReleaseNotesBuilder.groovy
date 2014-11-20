package org.mockito.release.notes.internal

import com.jcabi.github.Coordinates
import com.jcabi.github.Issue
import com.jcabi.github.Label
import com.jcabi.github.RtGithub;
import org.gradle.api.Project
import org.mockito.release.notes.PreviousVersionFromFile
import org.mockito.release.notes.ReleaseNotesBuilder
import org.mockito.release.notes.exec.Exec
import org.mockito.release.notes.improvements.Improvements
import org.mockito.release.notes.vcs.Commit
import org.mockito.release.notes.vcs.ContributionSet
import org.mockito.release.notes.vcs.Vcs

class DefaultReleaseNotesBuilder implements ReleaseNotesBuilder {

    private final Project project
    private final String gitHubToken
    private final String ignorePattern
    private final ImprovementsPrinter improvementsPrinter

    DefaultReleaseNotesBuilder(Project project, String gitHubToken, String ignorePattern,
                               ImprovementsPrinter improvementsPrinter) {
        this.ignorePattern = ignorePattern
        this.gitHubToken = gitHubToken
        this.project = project
        this.improvementsPrinter = improvementsPrinter
    }

    void updateNotes(File notesFile, String toVersion) {
        println "Updating release notes file: $notesFile"
        def currentContent = notesFile.text
        def previousVersion = "v" + new PreviousVersionFromFile(notesFile).getPreviousVersion() //TODO SF duplicated, reuse service
        println "Building notes since $previousVersion until $toVersion"
        def newContent = buildNotesBetween(previousVersion, toVersion)
        notesFile.text = newContent + currentContent
        println "Successfully updated the release notes!"
    }

    ContributionSet getContributionsBetween(String fromRevision, String toRevision) {
        return Vcs.getContributionsProvider(Exec.getGradleProcessRunner(project)).getContributionsBetween(fromRevision, toRevision);
    }

    String buildNotesBetween(String fromVersion, String toVersion) {
        ContributionSet contributions = getContributionsBetween(fromVersion, toVersion)
        def improvements = Improvements.getImprovementSetProvider().getImprovements(contributions);
        def date = new Date().format("yyyy-MM-dd HH:mm z", TimeZone.getTimeZone("UTC"))
        return """### $project.version ($date)

${contributions.toText()}
$improvements

"""
    }
}

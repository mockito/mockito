package org.mockito.release.notes

import org.gradle.api.Project
import org.mockito.release.notes.exec.Exec
import org.mockito.release.notes.improvements.Improvements
import org.mockito.release.notes.vcs.ContributionSet
import org.mockito.release.notes.vcs.Vcs
import org.mockito.release.notes.versions.PreviousVersionFromFile

class GitNotesBuilder implements NotesBuilder {

    private final Project project
    private final String authTokenEnvVar

    GitNotesBuilder(Project project, String authTokenEnvVar) {
        this.authTokenEnvVar = authTokenEnvVar
        this.project = project
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
        return Vcs.getGitProvider(Exec.getGradleProcessRunner(project)).getContributionsBetween(fromRevision, toRevision);
    }

    String buildNotesBetween(String fromVersion, String toVersion) {
        ContributionSet contributions = getContributionsBetween(fromVersion, toVersion)
        def improvements = Improvements.getGitHubProvider("GH_TOKEN").getImprovements(contributions);
        def date = new Date().format("yyyy-MM-dd HH:mm z", TimeZone.getTimeZone("UTC"))
        return """### $project.version ($date)

${contributions.toText()}
$improvements

"""
    }
}

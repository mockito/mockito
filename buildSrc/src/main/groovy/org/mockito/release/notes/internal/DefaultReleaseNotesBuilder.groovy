package org.mockito.release.notes.internal;

import com.jcabi.github.*
import org.gradle.api.Project
import org.mockito.release.notes.ContributionSet
import org.mockito.release.notes.GitCommit
import org.mockito.release.notes.Improvement
import org.mockito.release.notes.ImprovementSet
import org.mockito.release.notes.ReleaseNotesBuilder

class DefaultReleaseNotesBuilder implements ReleaseNotesBuilder {

    private final Project project
    private final String gitHubToken
    private final String ignorePattern

    DefaultReleaseNotesBuilder(Project project, String gitHubToken, String ignorePattern) {
        this.ignorePattern = ignorePattern
        this.gitHubToken = gitHubToken
        this.project = project
    }

    void updateNotes(File notesFile, String toVersion) {
        println "Updating release notes file: $notesFile"
        def currentContent = notesFile.text
        def previousVersion = "v" + getPreviousVersion(notesFile)
        println "Fetching $previousVersion"
        project.exec { commandLine "git", "fetch", "origin", "+refs/tags/$previousVersion:refs/tags/$previousVersion" }
        println "Building notes since $previousVersion until $toVersion"
        def newContent = buildNotesBetween(previousVersion, toVersion)
        notesFile.text = newContent + currentContent
        println "Successfully updated the release notes!"
    }

    String buildNotesBetween(String fromVersion, String toVersion) {
        def tickets = new HashSet()
        ContributionSet contributions = getContributionsBetween(fromVersion, toVersion)
        println "Parsing ${contributions.commitCount} commits"
        contributions.allCommits.each { GitCommit it ->
            def t = it.message.findAll("#\\d+")
            if (t) {
                tickets.addAll(t*.substring(1)) //get rid of leading '#'
            }

//this is helpful to find out what Google code issues we worked on:
//        def issues = it.findAll("[Ii]ssue \\d+")
//        if (issues) {
//            println "$issues found in $it"
//        }
        }
        ImprovementSet improvements = getImprovements(tickets)
        def date = new Date().format("yyyy-MM-dd HH:mm z", TimeZone.getTimeZone("UTC"))
        return """### $project.version ($date)

$contributions
$improvements

"""
    }

    ImprovementSet getImprovements(Set<String> tickets) {
        if (tickets.empty) {
            return new ImprovementSet(improvements: [])
        }
        //TODO we should query for all tickets via one REST call and stop using jcapi
        println "Querying GitHub API for ${tickets.size()} tickets. This may take a while."
        def github = new RtGithub(gitHubToken)
        def repo = github.repos().get(new Coordinates.Simple("mockito/mockito"))
        def issues = repo.issues()
        def out = []

        tickets.each {
            println " #$it"
            def i = issues.get(it as int)
            def issue = new Issue.Smart(i)
            if (issue.exists() && !issue.isOpen()) {
                out << new Improvement(id: issue.number(), title: issue.title(), url: issue.htmlUrl())
            }
        }
        new ImprovementSet(improvements: out, ignorePattern: ignorePattern)
    }

    String getPreviousVersion(File notesFile) {
        println "Attempting to figure out the previous version from the release notes file"
        return notesFile.withReader {
            def firstLine = it.readLine()
            assert firstLine.startsWith('###')
            //Example: "### 1.9.5 (06-10-2012)", we want to extract "1.9.5"
            def m = firstLine =~ /### (.+?) .*/
            assert m.matches()
            return m.group(1)
        }
    }

    ContributionSet getContributionsBetween(String fromRevision, String toRevision) {
        println "Loading all commits between $fromRevision and $toRevision"
        def out = new ByteArrayOutputStream()
        def entryToken = "@@commit@@"
        def infoToken = "@@info@@"
        project.exec {
            standardOutput = out
            commandLine "git", "log", "--pretty=format:%ae$infoToken%an$infoToken%B%N$entryToken", "${fromRevision}..${toRevision}"
        }
        def contributions = new ContributionSet()
        out.toString().split(entryToken).each { String logEntry ->
            def s = logEntry.split(infoToken)
            contributions.add(new GitCommit(email: s[0].trim(), author: s[1].trim(), message: s[2].trim()))
        }
        contributions
    }
}

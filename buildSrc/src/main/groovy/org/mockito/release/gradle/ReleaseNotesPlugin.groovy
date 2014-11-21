package org.mockito.release.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class ReleaseNotesPlugin implements Plugin<Project> {

    void apply(Project project) {
        //TODO SF have read-only auth token that is checked-in to the repo
        //TODO SF document how to work with release notes, e.g. that we only show closed tickets, etc.
        def notes = project.extensions.create(ReleaseNotesExtension.EXT_NAME, ReleaseNotesExtension, project.projectDir, project.version.toString())
        project.tasks.create("updateReleaseNotes") { Task task ->
            task.description = "Updates release notes file. Useful for previewing the release notes."
            task.doLast { notes.updateReleaseNotes() }
        }

        project.tasks.create("previewReleaseNotes") { Task task ->
            task.description = "Shows new content of release notes."
            task.doLast {
                def content = notes.getReleaseNotes()
                task.logger.lifecycle("----------------\n$content----------------")
            }
        }
    }
}

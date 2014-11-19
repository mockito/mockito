package org.mockito.release.notes;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.mockito.release.notes.internal.*;
import org.mockito.release.notes.PreviousVersionFromFile;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ReleaseNotesServices {

    private final Project project;
    private String gitHubToken;
    private String ignorePattern;
    private Map<String, String> labelToHeaderMapping = Collections.emptyMap();
    private List<String> labelsToIgnore = Collections.emptyList();
    private String headerForOtherImprovements;

    ReleaseNotesServices(Project project) {
        this.project = project;
    }

    ReleaseNotesBuilder createBuilder() {
        Collection<String> labelsToShowSeparately = labelToHeaderMapping.keySet();
        ImprovementSetSegregator segregator = new ImprovementSetSegregator(labelsToShowSeparately, labelsToIgnore);
        ImprovementsPrinter improvementsPrinter = new ImprovementsPrinter(segregator, labelToHeaderMapping, headerForOtherImprovements);
        if (gitHubToken == null) {
            throw new GradleException("GitHub token not provided");
        }
        return new DefaultReleaseNotesBuilder(project, gitHubToken, ignorePattern, improvementsPrinter);
    }

    //TODO SF interface
    ReleaseNotesServices gitHubToken(String gitHubToken) {
        this.gitHubToken = gitHubToken;
        return this;
    }

    //TODO: MZ: Not needed when labelsToIgnore are used
    ReleaseNotesServices ignoreImprovementsMatching(String pattern) {
        this.ignorePattern = pattern;
        return this;
    }

    ReleaseNotesServices ignoreImprovementsWithLabels(List<String> labelsToIgnore) {
        this.labelsToIgnore = labelsToIgnore;
        return this;
    }

    ReleaseNotesServices showSeparatelyImprovementsWithLabelMappings(Map<String, String> labelToHeaderMapping) {
        this.labelToHeaderMapping = labelToHeaderMapping;
        return this;
    }

    ReleaseNotesServices headerForOtherImprovements(String headerForOtherImprovements) {
        this.headerForOtherImprovements = headerForOtherImprovements;
        return this;
    }

    PreviousVersionProvider getPreviousVersionProvider(File releaseNotesFile) {
        return new PreviousVersionFromFile(releaseNotesFile);
    }
}

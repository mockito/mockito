package org.mockito.release.notes;

import org.gradle.api.Project;
import org.mockito.release.notes.internal.ImprovementSetSegregator;
import org.mockito.release.notes.internal.DefaultReleaseNotesBuilder;
import org.mockito.release.notes.internal.ImprovementsPrinter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ReleaseNotesBuilderFactory {

    private final Project project;
    private String gitHubToken;
    private String ignorePattern;
    private Map<String, String> labelToHeaderMapping = Collections.emptyMap();
    private List<String> labelsToIgnore = Collections.emptyList();
    private String headerForOtherImprovements;

    ReleaseNotesBuilderFactory(Project project) {
        this.project = project;
    }

    ReleaseNotesBuilder createBuilder() {
        Collection<String> labelsToShowSeparately = labelToHeaderMapping.keySet();
        ImprovementSetSegregator segregator = new ImprovementSetSegregator(labelsToShowSeparately, labelsToIgnore);
        ImprovementsPrinter improvementsPrinter = new ImprovementsPrinter(segregator, labelToHeaderMapping, headerForOtherImprovements);
        return new DefaultReleaseNotesBuilder(project, gitHubToken, ignorePattern, improvementsPrinter);
    }

    //TODO SF interface
    ReleaseNotesBuilderFactory gitHubToken(String gitHubToken) {
        this.gitHubToken = gitHubToken;
        return this;
    }

    //TODO: MZ: Not needed when labelsToIgnore are used
    ReleaseNotesBuilderFactory ignoreImprovementsMatching(String pattern) {
        this.ignorePattern = pattern;
        return this;
    }

    ReleaseNotesBuilderFactory ignoreImprovementsWithLabels(List<String> labelsToIgnore) {
        this.labelsToIgnore = labelsToIgnore;
        return this;
    }

    ReleaseNotesBuilderFactory showSeparatelyImprovementsWithLabelMappings(Map<String, String> labelToHeaderMapping) {
        this.labelToHeaderMapping = labelToHeaderMapping;
        return this;
    }

    ReleaseNotesBuilderFactory headerForOtherImprovements(String headerForOtherImprovements) {
        this.headerForOtherImprovements = headerForOtherImprovements;
        return this;
    }
}

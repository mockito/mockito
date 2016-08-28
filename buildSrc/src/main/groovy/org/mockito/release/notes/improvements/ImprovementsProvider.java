package org.mockito.release.notes.improvements;

import org.mockito.release.notes.vcs.ContributionSet;

import java.util.Map;

public interface ImprovementsProvider {

    ImprovementSet getImprovements(ContributionSet contributions, Map<String, String> labels);
}

package org.mockito.release.notes.improvements;

import org.mockito.release.notes.vcs.ContributionSet;

public interface ImprovementSetProvider {

    ImprovementSet getImprovements(ContributionSet contributions);
}

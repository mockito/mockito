package org.mockito.release.notes.vcs;

public interface ContributionsProvider {
    ContributionSet getContributionsBetween(String fromRev, String toRev);
}

package org.mockito.release.notes.vcs;

/**
 * Knows the contributions
 */
public interface ContributionsProvider {

    /**
     * Provides contributions between specified versions
     */
    ContributionSet getContributionsBetween(String fromRev, String toRev);
}

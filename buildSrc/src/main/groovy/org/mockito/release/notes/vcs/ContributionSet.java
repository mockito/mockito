package org.mockito.release.notes.vcs;

import java.util.Collection;

/**
 * A set of contributions
 */
public interface ContributionSet {

    /**
     * all commits in given contribution set, spanning all authors
     */
    Collection<Commit> getAllCommits();

    /**
     * human readable text representation
     */
    String toText();
}

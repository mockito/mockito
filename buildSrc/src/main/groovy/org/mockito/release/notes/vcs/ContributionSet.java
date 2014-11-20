package org.mockito.release.notes.vcs;

import org.mockito.release.notes.util.HumanReadable;

import java.util.Collection;

/**
 * A set of contributions
 */
public interface ContributionSet extends HumanReadable {

    /**
     * all commits in given contribution set, spanning all authors
     */
    Collection<Commit> getAllCommits();

    /**
     * human readable text representation
     */
    String toText();
}

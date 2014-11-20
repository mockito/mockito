package org.mockito.release.notes.vcs;

import java.util.Collection;

/**
 * Source code contribution by one single author
 */
interface Contribution {

    /**
     * all commits in this contribution
     */
    Collection<Commit> getCommits();

    /**
     * readable text representation of this contribution
     */
    String toText();
}

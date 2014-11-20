package org.mockito.release.notes.vcs;

import org.mockito.release.notes.util.Predicate;

/**
 * Ignores commits with [ci skip]
 */
class IgnoreCiSkip implements Predicate<Commit> {

    public boolean isTrue(Commit commit) {
        //we used #id for Travis CI build number in commits performed by Travis. Let's avoid pulling those ids here.
        //also, if ci was skipped we probably are not interested in such change, no?
        //Currently, all our [ci skip] are infrastructure commits plus documentation changes made by humans via github web interface
        return commit.getMessage().contains("[ci skip]");
    }
}

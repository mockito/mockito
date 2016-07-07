package org.mockito.release.notes.vcs;

import org.mockito.release.notes.util.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GitContributionsProvider implements ContributionsProvider {

    private static final Logger LOG = LoggerFactory.getLogger(GitContributionsProvider.class);
    private final GitLogProvider logProvider;
    private final Predicate<Commit> ignoredCommit;

    GitContributionsProvider(GitLogProvider logProvider, Predicate<Commit> ignoredCommit) {
        this.logProvider = logProvider;
        this.ignoredCommit = ignoredCommit;
    }

    public ContributionSet getContributionsBetween(String fromRev, String toRev) {
        LOG.info("Fetching {} from the repo", fromRev);

        LOG.info("Loading all commits between {} and {}", fromRev, toRev);

        String commitToken = "@@commit@@";
        String infoToken = "@@info@@";
        String log = logProvider.getLog(fromRev, toRev, "--pretty=format:%ae" + infoToken + "%an" + infoToken + "%B%N" + commitToken);

        DefaultContributionSet contributions = new DefaultContributionSet(ignoredCommit);

        for (String entry : log.split(commitToken)) {
            String[] entryParts = entry.split(infoToken);
            if (entryParts.length == 3) {
                String email = entryParts[0].trim();
                String author = entryParts[1].trim();
                String message = entryParts[2].trim();
                contributions.add(new GitCommit(email, author, message));
            }
        }

        return contributions;
    }
}

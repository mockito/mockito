package org.mockito.release.notes.vcs;

import org.mockito.release.notes.exec.ProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GitContributionsProvider implements ContributionsProvider {

    private static Logger LOG = LoggerFactory.getLogger(GitContributionsProvider.class);
    private final ProcessRunner runner;

    GitContributionsProvider(ProcessRunner runner) {
        this.runner = runner;
    }

    public ContributionSet getContributionsBetween(String fromRev, String toRev) {
        LOG.info("Fetching {} from the repo", fromRev);

        runner.run("git", "fetch", "origin", "+refs/tags/$previousVersion:refs/tags/$previousVersion");

        LOG.info("Loading all commits between {} and {}", fromRev, toRev);

        String entryToken = "@@commit@@";
        String infoToken = "@@info@@";
        String output = runner.run("git", "log", "--pretty=format:%ae" + infoToken + "%an" + infoToken + "%B%N" + entryToken, fromRev + ".." + toRev);

        DefaultContributionSet contributions = new DefaultContributionSet();

        for (String entry : output.split(entryToken)) {
            String[] entryParts = entry.split(infoToken);
            String email = entryParts[0].trim();
            String author = entryParts[1].trim();
            String message = entryParts[2].trim();
            contributions.add(new GitCommit(email, author, message));
        }

        return contributions;
    }
}

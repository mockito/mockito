package org.mockito.release.notes.improvements;

import org.mockito.release.notes.vcs.ContributionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class GitHubImprovementsProvider implements ImprovementsProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubImprovementsProvider.class);
    private final String authToken;

    public GitHubImprovementsProvider(String authToken) {
        this.authToken = authToken;
    }

    public ImprovementSet getImprovements(ContributionSet contributions, Map<String, String> labels) {
        LOGGER.info("Parsing {} commits with {} tickets", contributions.getAllCommits().size(), contributions.getAllTickets().size());
        DefaultImprovements out = new DefaultImprovements(labels);
        new GitHubTicketFetcher().fetchTickets(authToken, contributions.getAllTickets(), out);
        return out;
    }
}

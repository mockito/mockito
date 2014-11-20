package org.mockito.release.notes.improvements;

import org.mockito.release.notes.vcs.Commit;
import org.mockito.release.notes.vcs.ContributionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

class GitHubImprovementsProvider implements ImprovementsProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubImprovementsProvider.class);
    private final GitHubAuthToken authToken;

    public GitHubImprovementsProvider(GitHubAuthToken authToken) {
        this.authToken = authToken;
    }

    public ImprovementSet getImprovements(ContributionSet contributions) {
        LOGGER.info("Parsing {} commits", contributions.getAllCommits());
        Set<String> ticketIds = findTickets(contributions);
        DefaultImprovements out = new DefaultImprovements();
        new GitHubTicketFetcher().fetchTickets(authToken.getToken(), ticketIds, out);
        return out;
    }

    private Set<String> findTickets(ContributionSet contributions) {
        Set<String> tickets = new LinkedHashSet<String>();
        for (Commit commit : contributions.getAllCommits()) {
            Scanner scanner = new Scanner(commit.getMessage());
            Pattern ticket = Pattern.compile("#\\d+");
            while(scanner.hasNext(ticket)) {
                String ticketId = scanner.next(ticket).substring(1); //remove leading '#'
                tickets.add(ticketId);
            }

        }
        return tickets;
    }
}

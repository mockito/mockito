package org.mockito.release.notes.improvements;

import com.jcabi.github.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class GitHubTicketFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubTicketFetcher.class);

    void fetchTickets(String gitHubAuthToken, Collection<String> ticketIds, DefaultImprovements improvements) {
        if (ticketIds.isEmpty()) {
            return;
        }
        try {
            //TODO if possible we should query for all tickets via one REST call and perhaps stop using jcapi
            LOG.info("Querying GitHub API for {} tickets", ticketIds.size());
            RtGithub github = new RtGithub(gitHubAuthToken);
            Repo repo = github.repos().get(new Coordinates.Simple("mockito/mockito"));
            Issues issues = repo.issues();
            for (String ticketId : ticketIds) {
                LOG.info(" #{}", ticketId);
                //TODO make ticked id an int
                Issue i = issues.get(Integer.parseInt(ticketId));
                Issue.Smart issue = new Issue.Smart(i);
                if (issue.exists() && !issue.isOpen()) {
                    improvements.add(new Improvement(issue.number(), issue.title(), issue.htmlUrl().toString(),
                            labels(issue.labels())));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Problems fetching " + ticketIds.size() + " from GitHub", e);
        }
    }

    private static Set<String> labels(IssueLabels labels) {
        Set<String> out = new HashSet<String>();
        for (Label label : labels.iterate()) {
            out.add(label.name());
        }
        return out;
    }
}

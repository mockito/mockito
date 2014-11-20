package org.mockito.release.notes.improvements;

import com.jcabi.github.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

class GitHubTicketFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubTicketFetcher.class);

    ImprovementSet fetchTickets(String gitHubToken, Set<String> ticketIds) {
        if (ticketIds.isEmpty()) {
            return new DefaultImprovements();
        }
        try {
            //TODO if possible we should query for all tickets via one REST call and perhaps stop using jcapi
            LOG.info("Querying GitHub API for {} tickets", ticketIds.size());
            RtGithub github = new RtGithub(gitHubToken);
            Repo repo = github.repos().get(new Coordinates.Simple("mockito/mockito"));
            Issues issues = repo.issues();
            DefaultImprovements out = new DefaultImprovements();

            for (String ticketId : ticketIds) {
                LOG.info(" #{}", ticketId);
                //TODO make ticked id an int
                Issue i = issues.get(Integer.parseInt(ticketId));
                Issue.Smart issue = new Issue.Smart(i);
                if (issue.exists() && !issue.isOpen()) {
                    out.add(new Improvement(issue.number(), issue.title(), issue.htmlUrl(),
                            labels(issue.labels())));
                }
            }
            return out;
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

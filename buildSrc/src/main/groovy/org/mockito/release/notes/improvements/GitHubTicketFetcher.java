package org.mockito.release.notes.improvements;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.mockito.release.notes.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GitHubTicketFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubTicketFetcher.class);

    void fetchTickets(String authToken, Collection<String> ticketIds, DefaultImprovements improvements) {
        if (ticketIds.isEmpty()) {
            return;
        }
        LOG.info("Querying GitHub API for {} tickets", ticketIds.size());
        String url = "https://api.github.com/repos/mockito/mockito/issues?access_token=" + authToken;
        url += "&state=closed&filter=all";

        Set<Long> tickets = new HashSet<Long>();
        for (String id : ticketIds) {
            tickets.add(Long.parseLong(id));
        }

        try {
            fetch(tickets, improvements, url);
        } catch (Exception e) {
            throw new RuntimeException("Problems fetching " + ticketIds.size() + " from GitHub", e);
        }
    }

    private void fetch(Set<Long> tickets, DefaultImprovements improvements, String url) throws IOException {
        InputStream response = new URL(url).openStream();
        String content = IOUtil.readFully(response);
        LOG.info("GitHub API responded successfully.");
        List<JSONObject> issues = (List) JSONValue.parse(content);
        LOG.info("GitHub API returned {} issues.", issues.size());

        for (JSONObject issue : issues) {
            long id = (Long) issue.get("number");
            if (tickets.remove(id)) {
                String issueUrl = (String) issue.get("html_url");
                String title = (String) issue.get("title");
                improvements.add(new Improvement(id, title, issueUrl, new HashSet()));

                if (tickets.isEmpty()) {
                    return;
                }
            }
        }
    }
}

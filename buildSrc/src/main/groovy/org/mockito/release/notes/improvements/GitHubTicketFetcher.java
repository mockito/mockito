package org.mockito.release.notes.improvements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.mockito.release.notes.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GitHubTicketFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(GitHubTicketFetcher.class);

    void fetchTickets(String authToken, Collection<String> ticketIds, DefaultImprovements improvements) {
        if (ticketIds.isEmpty()) {
            return;
        }
        LOG.info("Querying GitHub API for {} tickets", ticketIds.size());

        Queue<Long> tickets = queuedTicketNumbers(ticketIds);

        try {
            GitHubIssues issues = GitHubIssues.authenticatingWith(authToken)
                    .state("closed")
                    .filter("all")
                    .direction("desc")
                    .browse();

            while (!tickets.isEmpty() && issues.hasNextPage()) {
                List<JSONObject> page = issues.nextPage();

                improvements.addAll(wantedImprovements(
                        dropTicketsAboveMaxInPage(tickets, page),
                        page));
            }
        } catch (Exception e) {
            throw new RuntimeException("Problems fetching " + ticketIds.size() + " from GitHub", e);
        }
    }

    private Queue<Long> dropTicketsAboveMaxInPage(Queue<Long> tickets, List<JSONObject> page) {
        if (page.isEmpty()) {
            return tickets;
        }
        Long highestId = (Long) page.get(0).get("number");
        while (!tickets.isEmpty() && tickets.peek() > highestId) {
            tickets.poll();
        }
        return tickets;
    }

    private Queue<Long> queuedTicketNumbers(Collection<String> ticketIds) {
        List<Long> tickets = new ArrayList<Long>();
        for (String id : ticketIds) {
            tickets.add(Long.parseLong(id));
        }
        Collections.sort(tickets);
        PriorityQueue<Long> longs = new PriorityQueue<Long>(tickets.size(), Collections.reverseOrder());
        longs.addAll(tickets);
        return longs;
    }

    private List<Improvement> wantedImprovements(Collection<Long> tickets, List<JSONObject> issues) {
        if(tickets.isEmpty()) {
            return Collections.emptyList();
        }

        ArrayList<Improvement> pagedImprovements = new ArrayList<Improvement>();
        for (JSONObject issue : issues) {
            long id = (Long) issue.get("number");
            if (tickets.remove(id)) {
                String issueUrl = (String) issue.get("html_url");
                String title = (String) issue.get("title");
                pagedImprovements.add(new Improvement(id, title, issueUrl, new HashSet<String>()));

                if (tickets.isEmpty()) {
                    return pagedImprovements;
                }
            }
        }
        return pagedImprovements;
    }


    private static class GitHubIssues {
        public static final String RELATIVE_LINK_NOT_FOUND = "none";
        private String nextPageUrl;

        private GitHubIssues(String authToken, String state, String filter, String direction) {
            // see API doc : https://developer.github.com/v3/issues/
            nextPageUrl = String.format("%s%s%s%s%s",
                    "https://api.github.com/repos/mockito/mockito/issues?access_token=" + authToken,
                    state == null ? "" : "&state=" + state,
                    filter == null ? "" : "&filter=" + filter,
                    direction == null ? "" : "&direction=" + direction,
                    "&page=1"
            );
        }


        public GitHubIssues init() throws IOException {
            URLConnection urlConnection = new URL(nextPageUrl).openConnection();
            nextPageUrl = extractRelativeLink(urlConnection.getHeaderField("Link"), "next");
            return this;
        }

        public boolean hasNextPage() {
            return !RELATIVE_LINK_NOT_FOUND.equals(nextPageUrl);
        }

        public List<JSONObject> nextPage() throws IOException {
            if(RELATIVE_LINK_NOT_FOUND.equals(nextPageUrl)) {
                throw new IllegalStateException("GitHub API no more issues to fetch");
            }
            URL url = new URL(nextPageUrl);
            LOG.info("GitHub API querying issue page {}", queryParamValue(url, "page"));
            URLConnection urlConnection = url.openConnection();

            LOG.info("GitHub API rate info => Remaining : {}, Limit : {}",
                    urlConnection.getHeaderField("X-RateLimit-Remaining"),
                    urlConnection.getHeaderField("X-RateLimit-Limit")
            );
            nextPageUrl = extractRelativeLink(urlConnection.getHeaderField("Link"), "next");

            return parseJsonFrom(urlConnection);
        }

        private String queryParamValue(URL url, String page) {
            String query = url.getQuery();
            for (String param : query.split("&")) {
                if(param.startsWith(page)) {
                    return param.substring(param.indexOf('=') + 1, param.length());
                }
            }
            return "N/A";
        }

        private List<JSONObject> parseJsonFrom(URLConnection urlConnection) throws IOException {
            InputStream response = urlConnection.getInputStream();

            String content = IOUtil.readFully(response);
            LOG.info("GitHub API responded successfully.");
            @SuppressWarnings("unchecked")
            List<JSONObject> issues = (List<JSONObject>) JSONValue.parse(content);
            LOG.info("GitHub API returned {} issues.", issues.size());
            return issues;
        }


        private String extractRelativeLink(String linkHeader, final String relativeType) {
            if (linkHeader == null) {
                return null;
            }

            // See GitHub API doc : https://developer.github.com/guides/traversing-with-pagination/
            // Link: <https://api.github.com/repositories/6207167/issues?access_token=a0a4c0f41c200f7c653323014d6a72a127764e17&state=closed&filter=all&page=2>; rel="next",
            //       <https://api.github.com/repositories/62207167/issues?access_token=a0a4c0f41c200f7c653323014d6a72a127764e17&state=closed&filter=all&page=4>; rel="last"
            for (String linkRel : linkHeader.split(",")) {
                if (linkRel.contains("rel=\"" + relativeType + "\"")) {
                    return linkRel.substring(
                            linkRel.indexOf("http"),
                            linkRel.indexOf(">; rel=\"" + relativeType + "\""));
                }
            }
            return RELATIVE_LINK_NOT_FOUND;
        }

        public static GitHubIssuesBuilder authenticatingWith(String authToken) {
            return new GitHubIssuesBuilder(authToken);
        }

        private static class GitHubIssuesBuilder {
            private final String authToken;
            private String state;
            private String filter;
            private String direction;

            public GitHubIssuesBuilder(String authToken) {
                this.authToken = authToken;
            }

            public GitHubIssuesBuilder state(String state) {
                this.state = state;
                return this;
            }

            public GitHubIssuesBuilder filter(String filter) {
                this.filter = filter;
                return this;
            }

            public GitHubIssuesBuilder direction(String direction) {
                this.direction = direction;
                return this;
            }

            public GitHubIssues browse() {
                return new GitHubIssues(authToken, state, filter, direction);
            }
        }
    }
}

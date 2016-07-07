package org.mockito.release.notes.vcs;

import java.util.Collection;
import java.util.Set;

class GitCommit implements Commit {

    private final String email;
    private final String author;
    private final String message;
    private final Set<String> tickets;

    public GitCommit(String email, String author, String message) {
        this.email = email;
        this.author = author;
        this.message = message;
        this.tickets = TicketParser.parseTickets(message);
    }

    public String getAuthorId() {
        return email;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public Collection<String> getTickets() {
        return tickets;
    }
}

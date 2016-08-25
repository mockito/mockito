package org.mockito.release.notes.vcs;

import java.util.Collection;

public interface Commit {

    /**
     * Author identifier. For git it would be 'email'
     */
    String getAuthorEmail();

    /**
     * Author display name. For git it would be 'author'
     */
    String getAuthorName();

    /**
     * Commit message
     */
    String getMessage();

    /**
     * Tickets referenced by the commit. For example, jira issue ids or GitHub issue ids.
     */
    Collection<String> getTickets();
}

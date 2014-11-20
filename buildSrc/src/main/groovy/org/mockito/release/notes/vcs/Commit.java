package org.mockito.release.notes.vcs;

public interface Commit {

    /**
     * Author identifier. For git it would be 'email'
     */
    String getAuthorId();

    /**
     * Author display name. For git it would be 'author'
     */
    String getAuthor();

    /**
     * Commit message
     */
    String getMessage();
}

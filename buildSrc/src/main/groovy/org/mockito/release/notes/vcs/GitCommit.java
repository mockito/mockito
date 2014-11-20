package org.mockito.release.notes.vcs;

class GitCommit implements Commit {

    private final String email;
    private final String author;
    private final String message;

    public GitCommit(String email, String author, String message) {
        this.email = email;
        this.author = author;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}

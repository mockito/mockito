package org.mockito.release.notes;

import java.util.LinkedList;
import java.util.List;

/**
 * Contribution of given author
 */
class Contribution {
    //email identifies the contributor, author alias not necessarily
    final String email;
    final String author;
    final List<GitCommit> commits = new LinkedList<GitCommit>();

    Contribution(GitCommit commit) {
        email = commit.email;
        author = commit.author;
        commits.add(commit);
    }

    void add(GitCommit commit) {
        assert email.equals(commit.email);
        commits.add(commit);
    }

    public String toString() {
        return author + ":" + commits.size();
    }
}
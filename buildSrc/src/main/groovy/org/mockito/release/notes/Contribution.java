package org.mockito.release.notes;

import java.util.LinkedList;
import java.util.List;

/**
 * Contribution of given author
 */
class Contribution {
    String email; //identifies the contributor
    String author;
    List<GitCommit> commits = new LinkedList<GitCommit>();

    void add(GitCommit commit) {
        if (email == null) {
            email = commit.email;
            author = commit.author;
            //TODO we could guess the best author from the ones associated with given email
            //we could base on existence of space (this hints that it's a proper first name + surname)
        }
        //email identifies the contributor, author alias not necessarily
        assert email.equals(commit.email);
        commits.add(commit);
    }

    public String toString() {
        return author + ":" + commits.size();
    }
}

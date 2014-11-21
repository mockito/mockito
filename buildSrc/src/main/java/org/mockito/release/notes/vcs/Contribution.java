package org.mockito.release.notes.vcs;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class Contribution implements Comparable<Contribution> {

    //email identifies the contributor, author alias not necessarily
    final String authorId;
    final String author;
    final List<Commit> commits = new LinkedList<Commit>();

    Contribution(Commit commit) {
        authorId = commit.getAuthorId();
        author = commit.getAuthor();
        commits.add(commit);
    }

    Contribution add(Commit commit) {
        assert authorId.equals(commit.getAuthorId());
        commits.add(commit);
        return this;
    }

    public String toText() {
        return commits.size() + ": " + author;
    }

    public int compareTo(Contribution other) {
        return Integer.valueOf(other.getCommits().size()).compareTo(commits.size());
    }

    public Collection<Commit> getCommits() {
        return commits;
    }
}

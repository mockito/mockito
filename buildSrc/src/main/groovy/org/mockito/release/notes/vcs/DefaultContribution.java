package org.mockito.release.notes.vcs;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class DefaultContribution implements Contribution, Comparable<DefaultContribution> {

    //email identifies the contributor, author alias not necessarily
    final String email;
    final String author;
    final List<Commit> commits = new LinkedList<Commit>();

    DefaultContribution(Commit commit) {
        email = commit.getEmail();
        author = commit.getAuthor();
        commits.add(commit);
    }

    void add(GitCommit commit) {
        assert email.equals(commit.getEmail());
        commits.add(commit);
    }

    public String toString() {
        return author + ":" + commits.size();
    }

    public int compareTo(DefaultContribution other) {
        return Integer.valueOf(commits.size()).compareTo(other.getCommits().size());
    }

    public Collection<Commit> getCommits() {
        return commits;
    }
}

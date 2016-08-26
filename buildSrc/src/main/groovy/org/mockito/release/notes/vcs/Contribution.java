package org.mockito.release.notes.vcs;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class Contribution implements Comparable<Contribution> {

    //email identifies the contributor, author alias not necessarily
    final String authorEmail;
    final String authorName;
    final List<Commit> commits = new LinkedList<Commit>();

    Contribution(Commit commit) {
        authorEmail = commit.getAuthorEmail();
        authorName = commit.getAuthorName();
        commits.add(commit);
    }

    Contribution add(Commit commit) {
        commits.add(commit);
        return this;
    }

    public String toText() {
        return commits.size() + ": " + authorName;
    }

    public int compareTo(Contribution other) {
        int byCommitCount = Integer.valueOf(other.getCommits().size()).compareTo(commits.size());
        if (byCommitCount != 0) {
            return byCommitCount;
        }
        return this.authorName.toUpperCase().compareTo(other.authorName.toUpperCase());
    }

    public Collection<Commit> getCommits() {
        return commits;
    }
}

package org.mockito.release.notes;

class Contribution {
    String email //identifies the contributor
    String author
    Collection<GitCommit> commits = new LinkedList<GitCommit>() //the commits
    void add(GitCommit commit) {
        if (email == null) {
            email = commit.email
            author = commit.author
            //TODO we could guess the best author from the ones associated with given email
            //we could base on existence of space (this hints that it's a proper first name + surname)
        }
        assert email == commit.email //email identifies the contributor, author alias not necessarily
        commits << commit
    }
    String toString() {
        "$author: ${commits.size()}"
    }
}

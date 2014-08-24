package org.mockito.release.notes;

class ContributionSet {
    Map<String, Contribution> contributions = new HashMap<String, Contribution>()
    int commitCount //total commit count
    void add(GitCommit commit) {
        if (commit.message.contains("[ci skip]")) {
            //we used #id for Travis CI build number in commits performed by Travis. Let's avoid pulling those ids here.
            //also, if ci was skipped we probably are not interested in such change, no?
            //Currently, all our [ci skip] are infrastructure commits
            return
        }
        commitCount++
        def c = contributions.get(commit.email)
        if (c == null) {
            contributions.put(commit.email, c = new Contribution())
        }
        c.add(commit)
    }
    Collection<GitCommit> getAllCommits() {
        def out = []
        contributions.values().each {
            out.addAll(it.commits)
        }
        out
    }
    String toString() {
        def sorted = contributions.sort { a, b -> b.value.commits.size() <=> a.value.commits.size() } //top contributors first
        def grouped = sorted.groupBy { it.value.commits.size() } //groupBy number of commits
        def out = """* Authors: ${contributions.keySet().size()}
* Commits: $commitCount"""
        grouped.each { k,v ->
            out += "\n  * $k: ${v.values()*.author.join(', ')}"
        }
        out
     }
}

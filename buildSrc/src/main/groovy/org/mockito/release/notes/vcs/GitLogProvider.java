package org.mockito.release.notes.vcs;

import org.mockito.release.exec.ProcessRunner;

class GitLogProvider {

    private final ProcessRunner runner;

    GitLogProvider(ProcessRunner runner) {
        this.runner = runner;
    }

    public String getLog(String fromRev, String toRev, String format) {
        runner.run("git", "fetch", "origin", "+refs/tags/" + fromRev + ":refs/tags/" + fromRev);
        return runner.run("git", "log", format, fromRev + ".." + toRev);
    }
}
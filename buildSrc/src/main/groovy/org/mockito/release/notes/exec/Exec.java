package org.mockito.release.notes.exec;

import java.io.File;

public class Exec {

    public static ProcessRunner getProcessRunner(File workDir) {
        return new DefaultProcessRunner(workDir);
    }
}

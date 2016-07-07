package org.mockito.release.exec;

import org.mockito.release.notes.util.ReleaseNotesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;

class DefaultProcessRunner implements ProcessRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultProcessRunner.class);
    private final File workDir;

    DefaultProcessRunner(File workDir) {
        this.workDir = workDir;
    }

    public String run(String... commandLine) {
        LOG.info("Executing command: {}", (Object) commandLine);

        try {
            Process process = new ProcessBuilder(commandLine).directory(workDir).redirectErrorStream(true).start();
            return readFully(new BufferedReader(new InputStreamReader(process.getInputStream())));
        } catch (Exception e) {
            throw new ReleaseNotesException("Problems executing command: " + Arrays.toString(commandLine), e);
        }
    }

    private static String readFully(BufferedReader reader) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } finally {
            reader.close();
        }
    }
}

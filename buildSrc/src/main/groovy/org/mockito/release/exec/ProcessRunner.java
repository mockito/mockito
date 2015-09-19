package org.mockito.release.exec;

/**
 * Provides ways to execute external processes
 */
public interface ProcessRunner {

    /**
     * Executes given command line and returns the output.
     *
     * @param commandLine to execute
     * @return combined error and standard output.
     */
    String run(String ... commandLine);
}

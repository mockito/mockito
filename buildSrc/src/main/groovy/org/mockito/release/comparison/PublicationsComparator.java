package org.mockito.release.comparison;

import groovy.lang.Closure;

import java.io.File;

/**
 * Compares publications
 */
public interface PublicationsComparator {

    /**
     * Sets files for comparison
     */
    void compareBinaries(Closure<File> left, Closure<File> right);

    /**
     * Sets poms for comparison
     */
    void comparePoms(Closure<String> left, Closure<String> right);

    /**
     * Gives information if publications are equal
     */
    boolean isPublicationsEqual();
}

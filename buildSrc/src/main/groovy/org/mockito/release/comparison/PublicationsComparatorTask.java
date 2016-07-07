package org.mockito.release.comparison;

import groovy.lang.Closure;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public class PublicationsComparatorTask extends DefaultTask implements PublicationsComparator {

    private final ZipComparator zipComparator = new ZipComparator(new ZipCompare());
    private final PomComparator pomComparator = new PomComparator();
    private Boolean publicationsEqual;

    public void compareBinaries(Closure<File> left, Closure<File> right) {
        zipComparator.setPair(left, right);
    }

    public void comparePoms(Closure<String> left, Closure<String> right) {
        pomComparator.setPair(left, right);
    }

    public boolean isPublicationsEqual() {
        assert publicationsEqual != null : "Comparison task was not executed yet, the 'publicationsEqual' information not available.";
        return publicationsEqual;
    }

    @TaskAction public void comparePublications() {
        getLogger().lifecycle("{} - about to compare publications", getPath());

        boolean poms = pomComparator.areEqual();
        getLogger().lifecycle("{} - pom files equal: {}", getPath(), poms);

        ZipComparator.Result result = zipComparator.compareFiles();
        getLogger().info("{} - compared binaries: '{}' and '{}'", getPath(), result.getFile1(), result.getFile2());
        boolean jars = result.areEqual();
        getLogger().lifecycle("{} - source jars equal: {}", getPath(), jars);


        this.publicationsEqual = jars && poms;
    }
}
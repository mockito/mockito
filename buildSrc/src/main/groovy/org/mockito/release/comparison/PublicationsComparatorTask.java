package org.mockito.release.comparison;

import groovy.lang.Closure;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public class PublicationsComparatorTask extends DefaultTask implements PublicationsComparator {

    private SourceJarComparator sourceJarComparator = new SourceJarComparator();
    private PomComparator pomComparator = new PomComparator();
    private Boolean publicationsEqual;

    void compareSourcesJar(Closure<File> left, Closure<File> right) {
        sourceJarComparator.setPair(left, right);
    }

    public boolean isPublicationsEqual() {
        assert publicationsEqual != null : "task not executed yet";
        return publicationsEqual;
    }

    void comparePom(Closure<String> left, Closure<String> right) {
        pomComparator.setPair(left, right);
    }

    @TaskAction public void comparePublications() {
        getLogger().lifecycle("{} - about to compare publications", getPath());

        boolean poms = pomComparator.areEqual();
        getLogger().lifecycle("{} - pom files equal: {}", getPath(), poms);

        boolean jars = sourceJarComparator.areEqual();
        getLogger().lifecycle("{} - source jars equal: {}", getPath(), jars);

        this.publicationsEqual = jars && poms;
    }
}
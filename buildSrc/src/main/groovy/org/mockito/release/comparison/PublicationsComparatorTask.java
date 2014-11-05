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
        sourceJarComparator.addPair(left, right);
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
        if (!poms) {
            getLogger().lifecycle("{} - pom files are not equal", getPath());
        }
        boolean jars = sourceJarComparator.areEqual();
        if (!jars) {
            getLogger().lifecycle("{} - source jars are not equal", getPath());
        }
        this.publicationsEqual = jars && poms;
    }
}
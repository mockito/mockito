package org.mockito.release.comparison;

import groovy.lang.Closure;

import java.io.File;

import static org.mockito.release.util.ArgumentValidation.notNull;

class SourceJarComparator {

    private Closure<File> left;
    private Closure<File> right;

    SourceJarComparator setPair(Closure<File> left, Closure<File> right) {
        notNull(left, "source jar file to compare", right, "source jar file to compare");
        this.left = left;
        this.right = right;
        return this;
    }

    boolean areEqual() {
        File left = this.left.call();
        File right = this.right.call();
        notNull(left, "source jar file to compare", right, "source jar file to compare");
        return left.length() == right.length();
    }
}

package org.mockito.release.comparison;

import groovy.lang.Closure;

import static org.mockito.release.util.ArgumentValidation.notNull;

class PomComparator {

    private Closure<String> left;
    private Closure<String> right;

    PomComparator setPair(Closure<String> left, Closure<String> right) {
        notNull(left, "pom content to compare", right, "pom content to compare");
        this.left = left;
        this.right = right;
        return this;
    }

    boolean areEqual() {
        String left = this.left.call();
        String right = this.right.call();

        notNull(left, "pom content to compare", right, "pom content to compare");

        return replaceVersion(left).equals(replaceVersion(right));
    }

    private String replaceVersion(String pom) {
        return pom.replaceFirst("<version>(.*)</version>", "<version>foobar</version>");
    }
}

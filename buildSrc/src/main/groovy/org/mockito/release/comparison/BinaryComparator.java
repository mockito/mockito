package org.mockito.release.comparison;

import groovy.lang.Closure;

import java.io.File;
import java.util.Arrays;

import static org.mockito.release.util.ArgumentValidation.notNull;

class BinaryComparator {

    private Closure<File> left;
    private Closure<File> right;

    BinaryComparator setPair(Closure<File> left, Closure<File> right) {
        notNull(left, "source jar file to compare", right, "source jar file to compare");
        this.left = left;
        this.right = right;
        return this;
    }

    boolean areEqual() {
        File left = this.left.call();
        File right = this.right.call();
        notNull(left, "source jar file to compare", right, "source jar file to compare");

        FileHasher hasher = new FileHasher();
        byte[] leftHash = hasher.hash(left);
        byte[] rightHash = hasher.hash(right);
        return Arrays.equals(leftHash, rightHash);
    }
}

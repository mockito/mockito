package org.mockito.release.notes.internal;

import java.util.List;

public class Improvement {
    String id;
    String title;
    String url;
    List<String> labels;
    public String toString() {
        return title + "[(#" + id + ")](" + url + ")";
    }
}

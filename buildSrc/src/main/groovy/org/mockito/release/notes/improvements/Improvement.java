package org.mockito.release.notes.improvements;

import java.util.Collection;

class Improvement {

    private final long id; //TODO SF String
    private final String title;
    private final String url;
    final Collection<String> labels;

    public Improvement(long id, String title, String url, Collection<String> labels) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.labels = labels;
    }

    public String toText() {
        return title + " [(#" + id + ")](" + url + ")";
    }
}

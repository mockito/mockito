package org.mockito.release.notes.improvements;

import java.net.URL;
import java.util.Collection;
import java.util.Set;

class Improvement {

    private final int id;
    private final String title;
    private final String url;
    private final Collection<String> labels;

    public Improvement(int id, String title, String url, Collection<String> labels) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.labels = labels;
    }

    public String toText() {
        return title + " [(#" + id + ")](" + url + ")";
    }
}

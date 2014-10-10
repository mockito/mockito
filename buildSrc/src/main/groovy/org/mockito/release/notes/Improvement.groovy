package org.mockito.release.notes;

class Improvement {
    String id
    String title
    String url
    List<String> labels
    String toString() { "$title [(#$id)]($url)" }
}

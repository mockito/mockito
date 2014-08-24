package org.mockito.release.notes;

class Improvement {
    String id
    String title
    String url
    String toString() { "$title [(#$id)]($url)" }
}

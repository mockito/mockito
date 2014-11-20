package org.mockito.release.notes.internal

import org.mockito.release.notes.improvements.Improvement

class ImprovementTestFixture {

    static Improvement anImprovementWithIdAndLabels(String id, List<String> labels) {
        new Improvement(id: id, title: "bug $id", url: "u$id", labels: labels)
    }
}

package org.mockito.release.notes.internal

class ImprovementTestFixture {

    static Improvement anImprovementWithIdAndLabels(String id, List<String> labels) {
        new Improvement(id: id, title: "bug $id", url: "u$id", labels: labels)
    }
}

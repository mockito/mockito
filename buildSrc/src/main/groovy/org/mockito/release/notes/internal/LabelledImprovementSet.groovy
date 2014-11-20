package org.mockito.release.notes.internal

import org.mockito.release.notes.improvements.Improvement

class LabelledImprovementSet implements ImprovementSet {
    private final Collection<Improvement> improvements
    private final String ignorePattern
    private final ImprovementsPrinter improvementsPrinter

    LabelledImprovementSet(Collection<Improvement> improvements, String ignorePattern, ImprovementsPrinter improvementsPrinter) {
        this.improvements = improvements.asImmutable()
        this.ignorePattern = ignorePattern
        this.improvementsPrinter = improvementsPrinter
    }

    String toString() {
        def i = ignorePattern == null? improvements : improvements.findAll { !(it.title =~ ignorePattern) }
        improvementsPrinter.print(i as List)
    }
}
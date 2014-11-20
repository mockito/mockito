package org.mockito.release.notes.internal

import org.mockito.release.notes.improvements.Improvement

class OneCategoryImprovementSet implements ImprovementSet {
    Collection<Improvement> improvements
    String ignorePattern

    String toString() {
        def i = ignorePattern == null? improvements : improvements.findAll { !(it.title =~ ignorePattern) }
        if (i.empty) {
            return "* Improvements: 0"
        }
        """* Improvements: ${i.size()}
  * ${i.join('\n  * ')}"""
    }
}
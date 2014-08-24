package org.mockito.release.notes;

class ImprovementSet {
    Collection<Improvement> improvements
    String toString() {
        if (improvements.empty) {
            return "* Improvements: 0"
        }
        """* Improvements: $count
  * ${improvements.join('\n  * ')}"""
    }
    int getCount() { improvements.size() }
}
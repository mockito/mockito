package org.mockito.release.notes.internal

import groovy.transform.PackageScope
import org.mockito.release.notes.improvements.Improvement

class ImprovementSetSegregator {

    @PackageScope
    static final NON_EXISTING_LABEL = "====NOT_EXISTING===="

    private final Collection<String> labelsToTokenize
    private final Collection<String> labelsToIgnore

    ImprovementSetSegregator(Collection<String> labelsToTokenize, Collection<String> labelsToIgnore) {
        this.labelsToTokenize = labelsToTokenize.asImmutable()
        this.labelsToIgnore = labelsToIgnore.asImmutable()
    }

    Map<String, List<Improvement>> tokenize(Collection<Improvement> improvements) {
        Map<String, List<Improvement>> tokenizedImprovements = labelsToTokenize.collectEntries { label ->
            [(label): improvements.findAll{ it.labels.contains(label)}]
        }
        tokenizedImprovements.put(NON_EXISTING_LABEL, getNonCategorizedChanges(improvements))
        tokenizedImprovements
    }

    private List<Improvement> getNonCategorizedChanges(Collection<Improvement> improvements) {
        List<Improvement> otherImprovements = improvements.findAll { improvement ->
            improvement.labels.disjoint(labelsToTokenize) && improvement.labels.intersect(labelsToIgnore).isEmpty()
        }
        otherImprovements
    }
}

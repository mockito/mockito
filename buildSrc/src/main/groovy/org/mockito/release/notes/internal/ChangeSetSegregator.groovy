package org.mockito.release.notes.internal

import groovy.transform.PackageScope
import org.mockito.release.notes.Improvement

class ChangeSetSegregator {

    // visible for testing
    @PackageScope
    static final NON_EXISTING_LABEL = "====NOT_EXISTING===="

    private final Collection<String> labelsToTokenize
    private final Collection<String> labelsToIgnore

    ChangeSetSegregator(Collection<String> labelsToTokenize, Collection<String> labelsToIgnore) {
        this.labelsToTokenize = labelsToTokenize.asImmutable()
        this.labelsToIgnore = labelsToIgnore.asImmutable()
    }

    Map<String, List<Improvement>> tokenize(Collection<Improvement> changes) {
        Map<String, List<Improvement>> tokenizedChanges = labelsToTokenize.collectEntries { label ->
            [(label): changes.findAll{ it.labels.contains(label)}]
        }
        tokenizedChanges.put(NON_EXISTING_LABEL, getNonCategorizedChanges(changes))
        tokenizedChanges
    }

    private List<Improvement> getNonCategorizedChanges(Collection<Improvement> changes) {
        List<Improvement> otherChanges = changes.findAll { change ->
            change.labels.disjoint(labelsToTokenize) && change.labels.intersect(labelsToIgnore).isEmpty()
        }
        otherChanges
    }
}

package org.mockito.release.notes.improvements;

import java.util.Set;

class DefaultImprovements implements ImprovementSet {

    private Set<Improvement> improvements;

    public String toText() {
        return null;
    }

    public void add(Improvement improvement) {
        improvements.add(improvement);
    }
}

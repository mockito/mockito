package org.mockito.release.notes.improvements;

import java.util.LinkedList;
import java.util.List;

class DefaultImprovements implements ImprovementSet {

    private final List<Improvement> improvements = new LinkedList<Improvement>();

    public String toText() {
        if (improvements.isEmpty()) {
            //TODO SF we should break the build if there are no notable improvements yet the binaries have changed
            return "* No notable improvements. See the commits for detailed changes.";
        }
        StringBuilder sb = new StringBuilder("* Improvements: ").append(improvements.size());
        for (Improvement i : improvements) {
            sb.append("\n  * ").append(i.toText());
        }
        return sb.toString();
    }

    public void add(Improvement improvement) {
        improvements.add(improvement);
    }

    public void addAll(List<Improvement> improvements) {
        this.improvements.addAll(improvements);
    }
}

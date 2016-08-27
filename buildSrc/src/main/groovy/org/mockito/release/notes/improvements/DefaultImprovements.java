package org.mockito.release.notes.improvements;

import org.mockito.release.util.MultiMap;

import java.util.*;

//TODO SF split the formatting from the data structure
class DefaultImprovements implements ImprovementSet {

    final List<Improvement> improvements = new LinkedList<Improvement>();
    private final Map<String, String> labels;

    public DefaultImprovements(Map<String, String> labels) {
        this.labels = labels;
    }

    public String toText() {
        if (improvements.isEmpty()) {
            return "* No notable improvements. See the commits for detailed changes.";
        }
        StringBuilder sb = new StringBuilder("* Improvements: ").append(improvements.size());
        MultiMap<String, Improvement> byLabel = new MultiMap<String, Improvement>();
        Set<Improvement> remainingImprovements = new LinkedHashSet<Improvement>(improvements);

        //Step 1, find improvements that match input labels
        //Iterate label first because the input labels determine the order
        for (String label : labels.keySet()) {
            for (Improvement i : improvements) {
                if (i.labels.contains(label) && remainingImprovements.contains(i)) {
                    remainingImprovements.remove(i);
                    byLabel.put(label, i);
                }
            }
        }

        //Step 2, print out the improvements that match input labels
        for (String label : byLabel.keySet()) {
            String labelCaption = labels.get(label);
            Collection<Improvement> labelImprovements = byLabel.get(label);
            sb.append("\n  * ").append(labelCaption).append(": ").append(labelImprovements.size());
            for (Improvement i : labelImprovements) {
                sb.append("\n    * ").append(i.toText());
            }
        }

        //Step 3, print out remaining changes
        if (!remainingImprovements.isEmpty()) {
            String indent;
            //We want clean view depending if there are labelled improvements or not
            if (byLabel.size() > 0) {
                indent = "  ";
                sb.append("\n  * Remaining changes: ").append(remainingImprovements.size());
            } else {
                indent = "";
            }

            for (Improvement i : remainingImprovements) {
                sb.append("\n").append(indent).append("  * ").append(i.toText());
            }
        }
        return sb.toString();
    }

    public DefaultImprovements add(Improvement improvement) {
        improvements.add(improvement);
        return this;
    }

    public DefaultImprovements addAll(List<Improvement> improvements) {
        this.improvements.addAll(improvements);
        return this;
    }
}

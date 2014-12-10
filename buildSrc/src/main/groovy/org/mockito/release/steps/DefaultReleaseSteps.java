package org.mockito.release.steps;

import org.mockito.release.util.operations.Operation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class DefaultReleaseSteps implements ReleaseSteps {

    private List<ReleaseStep> steps = new ArrayList<ReleaseStep>();

    public ReleaseStep newStep(String description, Operation operation) {
        DefaultReleaseStep step = new DefaultReleaseStep(description, operation);
        steps.add(step);
        return step;
    }

    public void perform() {
        System.out.println("Performing " + steps.size() + " release steps");
        List<ReleaseStep> attempted = new LinkedList<ReleaseStep>();
        for (ReleaseStep step : steps) {
            attempted.add(step);
            System.out.println("Step " + attempted.size() + ": " + step.getDescription());
            try {
                step.perform();
            } catch (Throwable t) {
                rollback(attempted); //TODO SF what if rollback fails?
                throw new RuntimeException("Release failed. Rollback was performed.", t);
            }
        }
    }

    private static void rollback(List<ReleaseStep> attempted) {
        System.out.println("Release failed. Rolling back " + attempted.size() + " release steps.");
        LinkedList<ReleaseStep> targets = new LinkedList<ReleaseStep>(attempted);
        while(!targets.isEmpty()) {
            ReleaseStep s = targets.removeLast();
            Operation r = s.getRollback();
            if (r != null) {
                System.out.println("Rolling back step " + (targets.size() + 1) + " (" + s.getDescription() + ")");
                r.perform();
            } else {
                System.out.println("No rollback for step " + (targets.size() + 1) + " (" + s.getDescription() + ")");
            }
        }
    }
}
package org.mockito.release.steps;

import org.mockito.release.util.operations.Operation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class DefaultReleaseSteps implements ReleaseSteps {

    private List<ReleaseStep> steps = new ArrayList<ReleaseStep>();

    public ConfigurableReleaseStep newStep(String description, Operation operation) {
        DefaultReleaseStep step = new DefaultReleaseStep(description, operation);
        steps.add(step);
        return step;
    }

  public ReleaseStep getStep(int stepNumber) {
    return steps.get(stepNumber - 1); //TODO SF array index
  }

  public void perform() {
        System.out.println("Performing " + steps.size() + " release steps");
        LinkedList<ReleaseStep> attempted = new LinkedList<ReleaseStep>();
        for (ReleaseStep step : steps) {
            attempted.add(step);
            System.out.println("Step " + attempted.size() + ": " + step.getDescription());
            try {
                step.perform();
            } catch (Throwable t) {
                rollback(attempted); //TODO SF what if rollback fails?
                throw new RuntimeException("Release failed at step " + attempted.size() + " (" + step.getDescription() + "). Rollback was performed.", t);
            }
        }
        //TODO SF needs tidy up. I should model better the cleanup VS rollback operation
        while(!attempted.isEmpty()) {
          ReleaseStep step = attempted.removeLast();
          Operation cleanup = step.getCleanup();
          if (cleanup != null) {
            System.out.println("Found cleanup operation for step " + (attempted.size() + 1) + " (" + step.getDescription() + ")");
            cleanup.perform();
          }
        }
    }

    private static void rollback(List<ReleaseStep> attempted) {
        System.out.println("Release failed. Rolling back " + attempted.size() + " release steps.");
        LinkedList<ReleaseStep> targets = new LinkedList<ReleaseStep>(attempted);
        while(!targets.isEmpty()) {
            ReleaseStep s = targets.removeLast();
            Operation r = s.getRollback();
            Operation c = s.getCleanup();
            if (r != null) {
              System.out.println("Rolling back step " + (targets.size() + 1) + " (" + s.getDescription() + ")");
              r.perform();
            } else if (c != null) {
              System.out.println("Cleaning up after step " + (targets.size() + 1) + " (" + s.getDescription() + ")");
              c.perform();
            } else {
              System.out.println("No rollback for step " + (targets.size() + 1) + " (" + s.getDescription() + ")");
            }
        }
    }
}
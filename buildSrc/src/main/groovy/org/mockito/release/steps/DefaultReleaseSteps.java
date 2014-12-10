package org.mockito.release.steps;

import org.mockito.release.util.operations.Operation;

import java.util.ArrayList;
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
        int i = 1;
        for (ReleaseStep step : steps) {
            System.out.println("Step " + (i++) + ": " + step.getDescription());
            step.perform();
        }
    }
}
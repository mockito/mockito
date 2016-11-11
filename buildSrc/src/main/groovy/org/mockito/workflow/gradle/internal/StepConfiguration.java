package org.mockito.workflow.gradle.internal;

import org.gradle.api.GradleException;
import org.gradle.api.Task;

import java.util.Map;

class StepConfiguration {
    private Map<String, Task> config;

    StepConfiguration(Map<String, Task> config) {
        this.config = config;
        validateConfig(config);
    }

    private static void validateConfig(Map<String, Task> config) {
        if (config.isEmpty()) {
            return;
        }
        if (config.size() > 1 || !(config.containsKey("rollback") || config.containsKey("cleanup"))) {
            throw new GradleException("Step configuration can only have one setting either 'rollback' or 'cleanup'. " +
                    "Please refer to the documentation.");
        }
        Map.Entry<String, Task> entry = config.entrySet().iterator().next();
        if (!(entry.getValue() instanceof Task)) {
            throw new GradleException("Step configuration '" + entry.getKey() + "' must refer to a Gradle task but it is: '" +
                   entry.getValue() + "'. Please refer to the documentation.");
        }
    }

    boolean isEmpty() {
        return config.isEmpty();
    }

    Task getRollback() {
        return config.get("rollback");
    }

    Task getCleanup() {
        return config.get("cleanup");
    }
}
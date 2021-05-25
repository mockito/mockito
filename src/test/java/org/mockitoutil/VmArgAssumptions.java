/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.lang.management.ManagementFactory;
import java.util.List;

public class VmArgAssumptions {
    public static void assumeVmArgPresent(String vmArg) {
        assumeTrue(assertEnabled(vmArg));
    }

    public static void assumeVmArgNotPresent(String vmArg) {
        assumeFalse(assertEnabled(vmArg));
    }

    private static boolean assertEnabled(String vmArg) {
        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String inputArgument : inputArguments) {
            if (inputArgument.contains(vmArg)) {
                return true;
            }
        }
        return false;
    }
}

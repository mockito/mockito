/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

public class VMNameContainsStrategy implements VMWarningStrategy {
    private final String vmName;
    private final String warningMessage;

    public VMNameContainsStrategy(String vmName, String warningMessage) {
        this.vmName = vmName;
        this.warningMessage = warningMessage;
    }

    @Override
    public boolean matches(String currentVMName) {
        return vmName != null && currentVMName.contains(vmName);
    }

    @Override
    public String getWarningMessage() {
        return warningMessage;
    }
}

/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.util;

import java.util.List;

import org.mockito.internal.exceptions.VerificationAwareInvocation;

public class ScenarioPrinter {

    public String print(List<VerificationAwareInvocation> invocations) {
        if (invocations.size() == 1) {
            return "Actually, above is the only interaction with this mock.";
        }
        StringBuilder sb = new StringBuilder(
                "***\n" +
                "For your reference, here is the list of all invocations ([?] - means unverified).\n");
        
        int counter = 0;
        for (VerificationAwareInvocation i : invocations) {
            sb.append(++counter).append(". ");
            if (!i.isVerified()) {
                sb.append("[?]");
            }
            sb.append(i.getLocation()).append("\n");
        }
        return sb.toString();
    }

}

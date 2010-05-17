package org.mockito.internal.exceptions.util;

import java.util.List;

import org.mockito.internal.exceptions.VerificationAwareInvocation;

public class ScenarioPrinter {

    public String print(List<VerificationAwareInvocation> invocations) {
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (VerificationAwareInvocation i : invocations) {
            sb.append(++counter + ". ");
            if (!i.isVerified()) {
                sb.append("[?]");
            }
            sb.append(i.getLocation() + "\n");
        }
        String scenario = sb.toString();
        return scenario;
    }

}

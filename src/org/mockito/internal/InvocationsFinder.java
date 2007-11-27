package org.mockito.internal;

import java.util.List;

public interface InvocationsFinder {

    List<Invocation> allInvocationsInOrder(List<Object> mocks);

}

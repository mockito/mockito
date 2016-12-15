/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.stubbing;

import org.mockito.Incubating;
import org.mockito.invocation.InvocationOnMock;

@Incubating
public interface ValidableAnswer {

    void validateFor(InvocationOnMock invocation);

}

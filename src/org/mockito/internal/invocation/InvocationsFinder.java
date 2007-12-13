/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.List;


public interface InvocationsFinder {

    List<Invocation> allInvocationsInOrder();

}

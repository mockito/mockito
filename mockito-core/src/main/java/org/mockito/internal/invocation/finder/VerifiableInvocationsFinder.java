/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.finder;

import java.util.List;
import java.util.stream.Collectors;

import org.mockito.invocation.Invocation;

/**
 * Author: Szczepan Faber, created at: 4/3/11
 */
public class VerifiableInvocationsFinder {

    private VerifiableInvocationsFinder() {}

    public static List<Invocation> find(List<?> mocks) {
        return AllInvocationsFinder.find(mocks).stream()
                .filter(invocation -> !invocation.isIgnoredForVerification())
                .collect(Collectors.toList());
    }
}

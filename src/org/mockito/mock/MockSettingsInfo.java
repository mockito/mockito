/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.mock;

import org.mockito.Incubating;
import org.mockito.internal.util.MockNameImpl;
import org.mockito.listeners.InvocationListener;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Set;

/**
 * Informs about the mock settings. An immutable view of {@link org.mockito.MockSettings}.
 */
@Incubating
public interface MockSettingsInfo {

    /**
     * if the mock is serializable, see {@link org.mockito.MockSettings#serializable}.
     */
    boolean isSerializable();

    /**
     * the invocation listeners attached to this mock, see {@link org.mockito.MockSettings#invocationListeners}.
     */
    List<InvocationListener> getInvocationListeners();

    /**
     * the default answer for this mock, see {@link org.mockito.MockSettings#defaultAnswer}.
     */
    Answer getDefaultAnswer();

    /**
     * the name of this mock, as printed on verification errors; see {@link org.mockito.MockSettings#name}.
     */
    MockNameImpl getMockName();

    void redefineMockName(String newName);

    Set<Class> getExtraInterfaces();

    Object getSpiedInstance();

    //TODO SF - forward needs to be consistently named with delegate
    //also figure this thing out.
    Object getDelegatedInstance();
}

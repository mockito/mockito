/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.util.*;

import org.easymock.IArgumentMatcher;
import org.easymock.internal.matchers.And;
import org.easymock.internal.matchers.Not;
import org.easymock.internal.matchers.Or;

public class LastControl {
    private static final ThreadLocal<MocksControl> threadToControl = new ThreadLocal<MocksControl>();

    public static synchronized void reportLastControl(MocksControl control) {
        if (control != null) {
            threadToControl.set(control);
        } else {
            threadToControl.remove();
        }
    }

    public static synchronized MocksControl lastControl() {
        return threadToControl.get();
    }
}

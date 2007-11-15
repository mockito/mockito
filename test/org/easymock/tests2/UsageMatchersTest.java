/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests2;

import static org.easymock.EasyMock.*;

import org.easymock.tests.IMethods;
import org.junit.Test;

public class UsageMatchersTest {
    @Test(expected = IllegalStateException.class)
    public void additionalMatchersFailAtReplay() {

        IMethods mock = createMock(IMethods.class);
        lt(5);

        replay(mock);
    }

}

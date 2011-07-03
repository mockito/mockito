/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;
import java.util.Set;

//see issue 230
public class DeepStubGenericTypesIssueTest extends TestBase {

    static interface ListSet extends List<Set> {}

    @Test
    @Ignore
    //TODO
    public void testDeepMockWithClass() {
        final ListSet mock = Mockito.mock(ListSet.class, Mockito.RETURNS_DEEP_STUBS);
        final Set mock2 = mock.get(0);
    }
}
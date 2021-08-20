/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.spies;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.inOrder;

import org.junit.Test;
import org.mockito.InOrder;

// https://github.com/mockito/mockito/issues/2394
public class SpiesWithRealEqualsAndInOrderTest {

    @Test
    public void should_be_able_to_handle_in_order_on_spies_with_equals() {
        ToBeSpied mock1 = spy(new ToBeSpied());
        ToBeSpied mock2 = spy(new ToBeSpied());
        mock1.someMethod();
        InOrder order = inOrder(mock1, mock2);
        order.verify(mock1).someMethod();
        order.verifyNoMoreInteractions();
    }

    static class ToBeSpied {
        void someMethod() {}
    }
}

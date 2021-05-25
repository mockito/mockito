/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.Test;
import org.mockito.Answers;

public class GenericArrayReturnTypeTest {

    @Test
    public void toArrayTypedDoesNotWork() throws Exception {
        Container container = mock(Container.class, Answers.RETURNS_DEEP_STUBS);
        container.getInnerContainer().getTheProblem().toArray(new String[] {});
    }

    class Container {

        private InnerContainer innerContainer;

        public InnerContainer getInnerContainer() {
            return innerContainer;
        }
    }

    class InnerContainer {

        private Set<String> theProblem;

        public Set<String> getTheProblem() {
            return theProblem;
        }
    }
}

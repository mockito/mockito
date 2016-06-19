package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockito.Answers;

import java.util.Set;

import static org.mockito.Mockito.mock;

public class GenericArrayReturnTypeTest {

    @Test
    public void toArrayTypedDoesNotWork() throws Exception {
        Container container = mock(Container.class, Answers.RETURNS_DEEP_STUBS);
        container.getInnerContainer().getTheProblem().toArray(new String[]{});
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

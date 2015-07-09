package org.mockitousage.bugs.creation;


import org.junit.Test;
import org.mockito.Mockito;
import org.mockitousage.bugs.creation.api.PublicClass;

// see GH-233
public class PublicMethodInParentWithNonPublicTypeInSignatureTest {

    private Object ref;

    @Test
    public void java_object_creation() throws Exception {
        ref = new PublicClass();
    }

    @Test
    public void should_not_fail_when_instantiating() throws Exception {
        ref = Mockito.mock(PublicClass.class);
    }
}

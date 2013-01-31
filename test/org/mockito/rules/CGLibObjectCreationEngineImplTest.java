package org.mockito.rules;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class CGLibObjectCreationEngineImplTest {

    static class TestClass {

    }

    private final CGLibObjectCreationEngineImpl engine = CGLibObjectCreationEngineImpl.getInstance();

    @Test
    public void shouldCreateObject() {
        // given

        // when
        final TestClass instance = engine.create(TestClass.class);

        // then

        assertThat(instance).isNotNull();
    }
}

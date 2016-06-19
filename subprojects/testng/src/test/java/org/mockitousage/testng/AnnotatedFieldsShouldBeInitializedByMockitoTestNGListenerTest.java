package org.mockitousage.testng;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Listeners(MockitoTestNGListener.class)
public class AnnotatedFieldsShouldBeInitializedByMockitoTestNGListenerTest {

    @Mock List<?> list;
    @Spy HashMap<?, ?> map;
    @InjectMocks SomeType someType;
    @Captor ArgumentCaptor<List<?>> captor;

    @Test
    public void ensure_annotated_fields_are_instantiated() throws Exception {
        assertThat(list).isNotNull();
        assertThat(map).isNotNull();
        assertThat(captor).isNotNull();
        assertThat(someType).isNotNull();
    }
}

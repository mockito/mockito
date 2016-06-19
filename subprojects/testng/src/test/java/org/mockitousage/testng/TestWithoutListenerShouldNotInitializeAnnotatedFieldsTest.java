package org.mockitousage.testng;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWithoutListenerShouldNotInitializeAnnotatedFieldsTest {

    @Mock List<?> list;
    @Spy Map<?, ?> map;
    @InjectMocks SomeType someType;
    @Captor ArgumentCaptor<List<?>> captor;

    @Test
    public void test_not_annotated_by_MockitoTestNGListener_should_not_touch_annotated_fields() throws Exception {
        assertThat(list).isNull();
        assertThat(map).isNull();
        assertThat(captor).isNull();
        assertThat(someType).isNull();
    }

}

package org.mockitousage.testng;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Listeners(MockitoTestNGListener.class)
@Test(enabled = false, description = "not yet ready")
public class CaptorAnnotatedFieldShouldBeClearedTest {

    @Captor ArgumentCaptor<String> captor;
    @Mock List<String> list;

    @Test(enabled = false)
    public void first_test_method_that_uses_captor() throws Exception {
        list.add("a");
        list.add("b");

        verify(list, times(2)).add(captor.capture());
        assertThat(captor.getAllValues()).containsOnly("a", "b");
    }

    @Test(enabled = false)
    public void second_test_method_that_uses_captor() throws Exception {
        list.add("t");
        list.add("u");

        verify(list, times(2)).add(captor.capture());
        assertThat(captor.getAllValues()).containsOnly("t", "u");
    }
}

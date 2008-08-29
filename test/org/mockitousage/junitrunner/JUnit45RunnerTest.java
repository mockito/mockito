package org.mockitousage.junitrunner;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitoutil.TestBase;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class JUnit45RunnerTest extends TestBase {
    
    @Mock private List list;
    
    @Test
    public void shouldInitMocksUsingRunner() {
        list.add("test");
        verify(list).add("test");
    }
}

package org.mockitousage;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
@Ignore("this issue is a little bit unclear to me. Let's wait until we have some more data")
public class ThreadReuseTest extends TestBase {
    
    private static ThreadLocal<Class> l = new ThreadLocal<Class>();
    @Mock IMethods mock;

    @Test(timeout = 100)
    public void test1() throws Exception {
        when(mock.simpleMethod()).thenReturn("foo");
        assertNull(l.get());
        l.set(getClass());
    }

    @Test
    public void test2() throws Exception {
        System.out.println(mock.simpleMethod());
        assertNull(l.get());
        l.set(getClass());
    }

    @Test(timeout = 100)
    public void test3() throws Exception {
        doThrow(new RuntimeException()).when(mock).voidMethod();
        assertNull(l.get());
        l.set(getClass());
    }

    @Test(timeout = 100)
    public void test4() throws Exception {
        mock.voidMethod();
        assertNull(l.get());
        l.set(getClass());
    }

//    @Test
//    public void test5() throws Exception {
//        when(mock.simpleMethod()).thenReturn("bar");
//        assertNull(l.get());
//        l.set(getClass());
//    }
}
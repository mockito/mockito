package org.mockitousage.basicapi;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.List;

public class SpyAnnotationTest extends TestBase {
    //TODO check if works when base class has initMocks()
	@Spy
	final List spiedList = new ArrayList();

	@Test
    public void shouldInitSpies() throws Exception {
        doReturn("foo").when(spiedList).get(10);

        assertEquals("foo", spiedList.get(10));
        assertTrue(spiedList.isEmpty());
    }

    @Test(expected = MockitoException.class)
    public void shouldFailIfFieldIsNotInitialized() throws Exception {
		class FailingSpy {
			@Spy private List mySpy;

			public List getMySpy() {
				return mySpy;
			}
		}

		MockitoAnnotations.initMocks(new FailingSpy());
    }

	@Test(expected = IndexOutOfBoundsException.class)
    public void shouldResetSpies() throws Exception {
        spiedList.get(10); // see shouldInitSpy
    }
	
	//TODO unsupported combinations of annotations (spy - captor, spy - mock)
	//TODO unsupported combinations of annotations (captor - mock)
}

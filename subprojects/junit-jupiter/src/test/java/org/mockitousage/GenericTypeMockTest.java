package org.mockitousage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GenericTypeMockTest {
    
    public static class UnderTest {
        List<String> stringListProvider;
        List<Integer> intListProvider;
    }

    @Mock
    private List<String> stringProviderMock;

    @Mock
    private List<Integer> intProviderMock;

    @InjectMocks
    private UnderTest underTest;

    /**
     * Verify that InjectMocks will correctly match fields with same generic type but different type parameters, 
     * without using the same field name.
     */
    @Test
    public void testInjectMock() {
        // this used to fail without any error message hinting at the problem, as soon as a class under test has 
        // a second field of the same generic type, but with different type parameter. The programmer then
        // had to know that mock field names have to match field names in the class under test.
        assertNotNull(underTest.stringListProvider);
        assertNotNull(underTest.intListProvider);
    }

}

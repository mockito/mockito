package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InjectionTwoListsWithDifferentTypeParameterTest {
    public static class TwoListsTest {
        private final List<Integer> integerList;
        private final List<String> stringList;

        TwoListsTest(List<String> argumentStringList, List<Integer> argumentIntegerList) {
            this.stringList = argumentStringList;
            this.integerList = argumentIntegerList;
        }

        public List<Integer> getIntegerList() {
            return integerList;
        }

        public List<String> getStringList() {
            return stringList;
        }
    }
    @InjectMocks
    TwoListsTest productionClass;

    @Mock(strictness = Mock.Strictness.LENIENT)
    List<Integer> mockIntegerList;

    @Mock(strictness = Mock.Strictness.LENIENT)
    List<String> mockStringList;
    @Test
    public void test() {
        System.out.println(productionClass.getIntegerList());
        System.out.println(productionClass.getStringList());
        assertEquals("mockIntegerList", productionClass.getIntegerList().toString());
        assertEquals("mockStringList", productionClass.getStringList().toString());
    }
}

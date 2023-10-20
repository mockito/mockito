package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class InjectionFourHashmapsWithDifferentTypeParameterTest {
    public static class TwoListsTest {
        private final HashMap<String, String> hashmap1;
        private final HashMap<String, Integer> hashmap2;
        private final HashMap<Float, Integer> hashmap3;
        private final HashMap<Float, Float> hashmap4;

        public TwoListsTest(
            HashMap<String, String> hashmap1, HashMap<String, Integer> hashmap2, HashMap<Float, Integer> hashmap3, HashMap<Float, Float> hashmap4) {
            this.hashmap1 = hashmap1;
            this.hashmap2 = hashmap2;
            this.hashmap3 = hashmap3;
            this.hashmap4 = hashmap4;
        }

        public HashMap<String, String> getHashmap1() {
            return hashmap1;
        }

        public HashMap<String, Integer> getHashmap2() {
            return hashmap2;
        }

        public HashMap<Float, Integer> getHashmap3() {
            return hashmap3;
        }

        public HashMap<Float, Float> getHashmap4() {
            return hashmap4;
        }
    }

    @Mock(strictness = Mock.Strictness.LENIENT)
    HashMap<String, String> mockhashmap1;

    @Mock(strictness = Mock.Strictness.LENIENT)
    HashMap<String, Integer> mockhashmap2;

    @Mock(strictness = Mock.Strictness.LENIENT)
    HashMap<Float, Float> mockhashmap4;
    @Mock(strictness = Mock.Strictness.LENIENT)
    HashMap<Float, Integer> mockhashmap3;

    @InjectMocks
    TwoListsTest productionClass;
    @Test
    public void test() {
        assertEquals("mockhashmap1", productionClass.getHashmap1().toString());
        assertEquals("mockhashmap2", productionClass.getHashmap2().toString());
        assertEquals("mockhashmap3", productionClass.getHashmap3().toString());
        assertEquals("mockhashmap4", productionClass.getHashmap4().toString());
    }
}

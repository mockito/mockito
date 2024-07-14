/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
public class GenericTypeMatchExtensiveGenericsTest {

    private final Usage1 usage1 = new Usage1();
    private final Usage2<String> usage2 = new Usage2<>();
    private final Usage2<Integer> usage2Invalid = new Usage2<>();
    private final Usage4<String> usage4 = new Usage4<>();
    private final Usage4<Integer> usage4Invalid = new Usage4<>();

    @ParameterizedTest
    @CsvSource({
        "usage1  ,usage1         ,base   ,usage1Field   ,true",
        "usage2  ,usage2         ,base   ,usage2Field   ,true",
        "usage2  ,usage2Invalid  ,base   ,usage2Field   ,false",
        "usage4  ,usage4         ,super  ,usage3FieldT  ,true",
        "usage4  ,usage4         ,super  ,usage3FieldU  ,true",
        "usage1  ,usage4         ,super  ,usage3FieldV  ,true",
        "usage4  ,usage4Invalid  ,super  ,usage3FieldT  ,false",
        "usage4  ,usage4Invalid  ,super  ,usage3FieldU  ,false",
        "usage1  ,usage4Invalid  ,super  ,usage3FieldV  ,true",
    })
    public void textExtensiveGenerics(
            String sourceFieldName,
            String targetInstanceName,
            String fieldOfBaseOrSuper,
            String targetFieldName,
            boolean expectedMatch)
            throws ReflectiveOperationException {
        Field sourceField =
                GenericTypeMatchExtensiveGenericsTest.class.getDeclaredField(sourceFieldName);
        Field containingField =
                GenericTypeMatchExtensiveGenericsTest.class.getDeclaredField(targetInstanceName);
        Field targetField =
                "base".equals(fieldOfBaseOrSuper)
                        ? containingField.getType().getDeclaredField(targetFieldName)
                        : containingField
                                .getType()
                                .getSuperclass()
                                .getDeclaredField(targetFieldName);
        GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
        GenericTypeMatch containingMatch = GenericTypeMatch.ofField(containingField);
        Optional<GenericTypeMatch> optTargetMatch = containingMatch.findDeclaredField(targetField);
        assertTrue(optTargetMatch.isPresent());
        assertEquals(expectedMatch, optTargetMatch.get().matches(sourceMatch));
    }

    interface Interface0<P0> {
        P0 getParamInterface0();
    }

    interface Interface1<P1, P2 extends Interface1<P1, P2>> extends Interface0<P2> {
        @Override
        P2 getParamInterface0();

        P1 getParamInterface1();
    }

    interface Interface2<P3 extends Interface2<P3, P4>, P4> extends Interface1<P4, P3> {}

    interface Interface3<
                    P5 extends Interface3<P5, P6, P7>,
                    P6 extends Interface1<P7, P6> & Interface2<P6, P7>,
                    P7>
            extends Interface1<P7, P6>, Interface2<P6, P7> {
        @Override
        P6 getParamInterface0();

        @Override
        P7 getParamInterface1();

        P5 getParamInterface3();
    }

    interface Interface4<P8 extends Interface4<P8>>
            extends Interface3<P8, P8, P8>, Interface1<P8, P8> {
        @Override
        P8 getParamInterface0();
    }

    static class Usage1 implements Interface4<Usage1> {
        private final Usage1 usage1Field = this;

        @Override
        public Usage1 getParamInterface0() {
            return usage1Field;
        }

        @Override
        public Usage1 getParamInterface1() {
            return usage1Field;
        }

        @Override
        public Usage1 getParamInterface3() {
            return usage1Field;
        }
    }

    static class Usage2<T> implements Interface4<Usage2<T>> {
        private final Usage2<T> usage2Field = this;

        @Override
        public Usage2<T> getParamInterface0() {
            return usage2Field;
        }

        @Override
        public Usage2<T> getParamInterface1() {
            return usage2Field;
        }

        @Override
        public Usage2<T> getParamInterface3() {
            return usage2Field;
        }
    }

    static class Usage3<T extends Usage3<T, U, V>, U extends T, V> implements Interface3<T, U, V> {
        private T usage3FieldT;
        private U usage3FieldU;
        private V usage3FieldV;

        @Override
        public U getParamInterface0() {
            return usage3FieldU;
        }

        @Override
        public V getParamInterface1() {
            return usage3FieldV;
        }

        @Override
        public T getParamInterface3() {
            return usage3FieldU;
        }
    }

    static class Usage4<T> extends Usage3<Usage4<T>, Usage4<T>, Usage1> {}
}

package org.mockito.internal.util.reflection.generic;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
public class GenericTypeMatchTest {

    public static final String STATIC_CLASS_PREFIX = GenericTypeMatchTest.class.getName() + "$";

    @Nested
    public class SimpleTypesTest {

        private Integer integer;
        private Number number;
        private String string;

        @ParameterizedTest
        @CsvSource({
            "integer  ,integer  ,true",
            "integer  ,number   ,true",
            "number   ,integer  ,false",
            "integer  ,string   ,false",
            "string   ,integer  ,false"
        })
        public void testSimpleTypes(String sourceFieldName, String targetFieldName, boolean matches) throws NoSuchFieldException {
            Field sourceField = SimpleTypesTest.class.getDeclaredField(sourceFieldName);
            Field targetField = SimpleTypesTest.class.getDeclaredField(targetFieldName);
            GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
            GenericTypeMatch targetMatch = GenericTypeMatch.ofField(targetField);
            assertEquals(matches, targetMatch.matches(sourceMatch));
        }

    }

    @Nested
    public class ParameterizedTypesTest {

        private List<Integer> integerList;
        private Collection<Integer> integerCollection;
        private List<String> stringList;
        private List<?> wildcardList;
        private List<? extends Integer> wildcardListInteger;
        private Map<String, Integer> mapStringInteger;
        private Map<?, ?> wildcardMap;
        private Map<? extends String, ? extends Number> wildcardMapStringNumber;

        @ParameterizedTest
        @CsvSource({
            "integerList          ,integerList          ,true",
            "integerList          ,integerCollection    ,true",
            "integerCollection    ,integerList          ,false",
            "integerList          ,stringList           ,false",
            "stringList           ,integerList          ,false",

            "stringList           ,wildcardList         ,true",
            "wildcardList         ,stringList           ,false",
            "wildcardList         ,wildcardList         ,true",
            "wildcardListInteger  ,integerList          ,true",
            "wildcardListInteger  ,stringList           ,false",
            "wildcardListInteger  ,wildcardListInteger  ,true",
            "wildcardListInteger  ,wildcardList         ,true",
            "wildcardList         ,wildcardListInteger  ,false",

            "mapStringInteger         ,mapStringInteger         ,true",
            "wildcardMap              ,mapStringInteger         ,false",
            "wildcardMapStringNumber  ,mapStringInteger         ,false",
            "mapStringInteger         ,wildcardMap              ,true",
            "wildcardMap              ,wildcardMap              ,true",
            "wildcardMapStringNumber  ,wildcardMap              ,true",
            "mapStringInteger         ,wildcardMapStringNumber  ,true",
            "wildcardMapStringNumber  ,wildcardMapStringNumber  ,true",
            "wildcardMap              ,wildcardMapStringNumber  ,false",
        })
        public void testParameterizedTypes(String sourceFieldName, String targetFieldName, boolean matches) throws NoSuchFieldException {
            Field sourceField = ParameterizedTypesTest.class.getDeclaredField(sourceFieldName);
            Field targetField = ParameterizedTypesTest.class.getDeclaredField(targetFieldName);
            GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
            GenericTypeMatch targetMatch = GenericTypeMatch.ofField(targetField);
            assertEquals(matches, targetMatch.matches(sourceMatch));
        }
    }

    @Nested
    public class ArrayTypesTest {

        private Integer[] integerArray;
        private String[] stringArray;
        private Object[] objectArray;

        @ParameterizedTest
        @CsvSource({
            "integerArray  ,integerArray  ,true",
            "stringArray   ,integerArray  ,false",
            "objectArray   ,integerArray  ,false",
            "stringArray   ,stringArray   ,true",
            "integerArray  ,stringArray   ,false",
            "objectArray   ,stringArray   ,false",
            "objectArray   ,objectArray   ,true",
            "stringArray   ,objectArray   ,true",
            "integerArray  ,objectArray   ,true",
        })
        public void testArrayTypes(String sourceFieldName, String targetFieldName, boolean matches) throws NoSuchFieldException {
            Field sourceField = ArrayTypesTest.class.getDeclaredField(sourceFieldName);
            Field targetField = ArrayTypesTest.class.getDeclaredField(targetFieldName);
            GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
            GenericTypeMatch targetMatch = GenericTypeMatch.ofField(targetField);
            assertEquals(matches, targetMatch.matches(sourceMatch));
        }
    }

    static class CollectionBox<T> {
        private Collection<T> collection;
    }

    static class SubOfBox<S> extends CollectionBox<S> {}

    static class ConcreteSubOfBox extends CollectionBox<Integer> {}

    static class SubOfSubOfBox<S> extends SubOfBox<S> {}

    static class SubOfConcrete extends ConcreteSubOfBox {}

    static abstract class Change {}

    static class ChangeCollection<T extends Change> {
        private List<T> changes;
    }

    @Nested
    public class NestedFieldTest {

        private CollectionBox<Integer> integerCollectionBox;
        private CollectionBox<String> stringCollectionBox;
        private CollectionBox<?> wildcardCollectionBox;
        @SuppressWarnings("rawtypes")
        private CollectionBox rawCollectionBox;

        private SubOfBox<Integer> integerSubOfBox;
        private SubOfBox<String> stringSubOfBox;
        private SubOfBox<?> wildcardSubOfBox;

        private ConcreteSubOfBox concreteSubOfBox;
        private SubOfConcrete subOfConcrete;

        private SubOfSubOfBox<Integer> integerSubOfSubOfBox;
        private SubOfSubOfBox<String> stringSubOfSubOfBox;

        @SuppressWarnings("rawtypes")
        private ChangeCollection changesTargetRaw;

        private List<Integer> integerList;
        private List<String> stringList;
        private Collection<?> wildcardCollection;

        private List<Change> changeList;

        @ParameterizedTest
        @CsvSource({
            "integerList         ,integerCollectionBox   ,CollectionBox  ,collection  ,true",
            "integerList         ,stringCollectionBox    ,CollectionBox  ,collection  ,false",
            "integerList         ,wildcardCollectionBox  ,CollectionBox  ,collection  ,false",
            "wildcardCollection  ,wildcardCollectionBox  ,CollectionBox  ,collection  ,false",

            "integerList         ,integerSubOfBox        ,CollectionBox  ,collection  ,true",
            "integerList         ,stringSubOfBox         ,CollectionBox  ,collection  ,false",
            "integerList         ,wildcardSubOfBox       ,CollectionBox  ,collection  ,false",
            "wildcardCollection  ,wildcardSubOfBox       ,CollectionBox  ,collection  ,false",

            "integerList         ,concreteSubOfBox       ,CollectionBox  ,collection  ,true",
            "stringList          ,concreteSubOfBox       ,CollectionBox  ,collection  ,false",
            "integerList         ,subOfConcrete          ,CollectionBox  ,collection  ,true",

            "integerList         ,integerSubOfSubOfBox   ,CollectionBox  ,collection  ,true",
            "integerList         ,stringSubOfSubOfBox    ,CollectionBox  ,collection  ,false",

            "integerList         ,rawCollectionBox       ,CollectionBox  ,collection  ,true",
            "stringList          ,rawCollectionBox       ,CollectionBox  ,collection  ,true",

            "changeList          ,changesTargetRaw       ,ChangeCollection  ,changes  ,true",
            "integerList         ,changesTargetRaw       ,ChangeCollection  ,changes  ,false",
        })
        public void testNestedFields(String sourceFieldName,
                                     String containingFieldName,
                                     String targetClassName,
                                     String targetFieldName,
                                     boolean matches) throws NoSuchFieldException, ClassNotFoundException {
            Field sourceField = NestedFieldTest.class.getDeclaredField(sourceFieldName);
            Field containingField = NestedFieldTest.class.getDeclaredField(containingFieldName);
            String className = STATIC_CLASS_PREFIX + targetClassName;
            Class<?> targetClass = Class.forName(className);
            Field targetField = targetClass.getDeclaredField(targetFieldName);
            GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
            GenericTypeMatch containingMatch = GenericTypeMatch.ofField(containingField);
            Optional<GenericTypeMatch> optTargetMatch = containingMatch.findDeclaredField(targetField);
            assertTrue(optTargetMatch.isPresent());
            assertEquals(matches, optTargetMatch.get().matches(sourceMatch));
        }
    }

    static class Box<X> {}

    static class WithBox<T> {
        Box<T> box;
    }

    static class WithBoxArray<T> {
        Box<T>[] boxArray;
    }

    static class WithArrayBox<T> {
        Box<T[]> arrayBox;
    }

    static class WithBoxArrayTwoDim<T> {
        private Box<T>[][] boxArrayTwoDim;
    }

    static class ArgToArray<T> extends WithBox<T[]> {}
    static class ArgToArrayTwoDim<T> extends WithBox<T[][]> {}
    static class ArgToArrayWithArray<T> extends WithBoxArray<T[]> {}

    @Nested
    public class GenericArrayTypesTest {

        private WithBoxArray<String> withStringBoxArray;
        private WithBoxArrayTwoDim<String> withStringBoxArrayTwoDim;
        private WithArrayBox<String> withStringArrayBox;
        private ArgToArray<String> stringArgToArray;
        private ArgToArrayTwoDim<String> stringArgToArrayTwoDim;
        private ArgToArrayWithArray<String> stringArgToArrayWithArray;
        private ArgToArray<?> wildcardArgToArray;

        private Box<String>[] stringBoxArray;
        private Box<Integer>[] integerBoxArray;
        private Box<String>[][] stringBoxArrayTwoDim;
        private Box<Integer>[][] integerBoxArrayTwoDim;
        private Box<String[]> stringArrayBox;
        private Box<Integer[]> integerArrayBox;
        private Box<String[][]> stringArrayBoxTwoDim;
        private Box<String[]>[] stringArrayBoxArray;
        private Box<?> wildcardBox;

        @ParameterizedTest
        @CsvSource({
            "stringBoxArray         ,withStringBoxArray         ,WithBoxArray        ,boxArray        ,true",
            "integerBoxArray        ,withStringBoxArray         ,WithBoxArray        ,boxArray        ,false",
            "stringBoxArrayTwoDim   ,withStringBoxArray         ,WithBoxArray        ,boxArray        ,false",

            "stringBoxArrayTwoDim   ,withStringBoxArrayTwoDim   ,WithBoxArrayTwoDim  ,boxArrayTwoDim  ,true",
            "integerBoxArrayTwoDim  ,withStringBoxArrayTwoDim   ,WithBoxArrayTwoDim  ,boxArrayTwoDim  ,false",
            "stringBoxArray         ,withStringBoxArrayTwoDim   ,WithBoxArrayTwoDim  ,boxArrayTwoDim  ,false",

            "stringArrayBox         ,stringArgToArray           ,WithBox             ,box             ,true",
            "integerArrayBox        ,stringArgToArray           ,WithBox             ,box             ,false",
            "wildcardBox            ,stringArgToArray           ,WithBox             ,box             ,false",
            "stringArrayBox         ,wildcardArgToArray         ,WithBox             ,box             ,false",
            "integerArrayBox        ,wildcardArgToArray         ,WithBox             ,box             ,false",
            "wildcardBox            ,wildcardArgToArray         ,WithBox             ,box             ,false",
            "stringArrayBoxTwoDim   ,stringArgToArrayTwoDim     ,WithBox             ,box             ,true",
            "stringArrayBox         ,stringArgToArrayTwoDim     ,WithBox             ,box             ,false",

            "stringArrayBox         ,withStringArrayBox         ,WithArrayBox        ,arrayBox        ,true",
            "integerArrayBox        ,withStringArrayBox         ,WithArrayBox        ,arrayBox        ,false",

            "stringArrayBoxArray    ,stringArgToArrayWithArray  ,WithBoxArray        ,boxArray        ,true",
            "stringArrayBox         ,stringArgToArrayWithArray  ,WithBoxArray        ,boxArray        ,false",
            "stringBoxArray         ,stringArgToArrayWithArray  ,WithBoxArray        ,boxArray        ,false",
        })
        public void testGenericArrayTypes(String sourceFieldName,
                                          String containingFieldName,
                                          String targetClassName,
                                          String targetFieldName,
                                          boolean matches) throws NoSuchFieldException, ClassNotFoundException {
            Field sourceField = GenericArrayTypesTest.class.getDeclaredField(sourceFieldName);
            Field containingField = GenericArrayTypesTest.class.getDeclaredField(containingFieldName);
            String className = STATIC_CLASS_PREFIX + targetClassName;
            Class<?> targetClass = Class.forName(className);
            Field targetField = targetClass.getDeclaredField(targetFieldName);
            GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
            GenericTypeMatch containingMatch = GenericTypeMatch.ofField(containingField);
            Optional<GenericTypeMatch> optTargetMatch = containingMatch.findDeclaredField(targetField);
            assertTrue(optTargetMatch.isPresent());
            assertEquals(matches, optTargetMatch.get().matches(sourceMatch));
        }
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

    interface Interface3<P5 extends Interface3<P5, P6, P7>, P6 extends Interface1<P7, P6> & Interface2<P6, P7>, P7>
        extends Interface1<P7, P6>, Interface2<P6, P7> {
        @Override
        P6 getParamInterface0();
        @Override
        P7 getParamInterface1();
        P5 getParamInterface3();
    }

    interface Interface4<P8 extends Interface4<P8>> extends Interface3<P8, P8, P8>, Interface1<P8, P8> {
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

    @Nested
    public class ExtensiveGenericsTest {

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
        public void textExtensiveGenerics(String sourceFieldName,
                                          String targetInstanceName,
                                          String fieldOfBaseOrSuper,
                                          String targetFieldName,
                                          boolean expectedMatch) throws ReflectiveOperationException {
            Field sourceField = ExtensiveGenericsTest.class.getDeclaredField(sourceFieldName);
            Field containingField = ExtensiveGenericsTest.class.getDeclaredField(targetInstanceName);
            Field targetField = "base".equals(fieldOfBaseOrSuper)
                ? containingField.getType().getDeclaredField(targetFieldName)
                : containingField.getType().getSuperclass().getDeclaredField(targetFieldName);
            GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
            GenericTypeMatch containingMatch = GenericTypeMatch.ofField(containingField);
            Optional<GenericTypeMatch> optTargetMatch = containingMatch.findDeclaredField(targetField);
            assertTrue(optTargetMatch.isPresent());
            assertEquals(expectedMatch, optTargetMatch.get().matches(sourceMatch));
        }
    }
}

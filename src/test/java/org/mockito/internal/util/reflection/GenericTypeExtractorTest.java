package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockitoutil.TestBase;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.internal.util.reflection.GenericTypeExtractor.genericTypeOf;

public class GenericTypeExtractorTest extends TestBase {

    class Base<T> {}
    static class StaticBase<T> {}
    interface IBase<T> {}
    interface StaticIBase<T> {}

    class IntImpl extends Base<Integer> {}
    static class StaticIntImpl extends StaticBase<Integer> {}
    class NestedImpl extends Base<Base<String>> {}
    class NonGeneric extends Base {}

    class IIntImpl implements IBase<Integer>{}
    class INestedImpl implements IBase<IBase<String>>{}
    class INonGeneric implements IBase {}
    class Mixed extends Base<Integer> implements IBase<String> {}

    class Deeper extends IntImpl implements Serializable {}
    class EvenDeeper extends Deeper implements Cloneable {}
    interface Iface extends IBase<Integer> {}
    interface IDeeper extends Serializable, Iface, Cloneable {}

    interface Crazy extends Serializable, IDeeper, Cloneable {}
    class Crazier extends EvenDeeper implements Crazy {}

    @Test public void finds_generic_type() {
        assertEquals(Integer.class, genericTypeOf(IntImpl.class, Base.class, IBase.class));
        assertEquals(Integer.class, genericTypeOf(StaticIntImpl.class, StaticBase.class, IBase.class));

        assertEquals(Object.class, genericTypeOf(NestedImpl.class, Base.class, IBase.class));
        assertEquals(Object.class, genericTypeOf(NonGeneric.class, Base.class, IBase.class));
        assertEquals(Object.class, genericTypeOf(String.class, Base.class, IBase.class));
        assertEquals(Object.class, genericTypeOf(String.class, List.class, Map.class));

        assertEquals(String.class, genericTypeOf(new Base<String>() {}.getClass(), Base.class, IBase.class));
        assertEquals(String.class, genericTypeOf(new IBase<String>() {}.getClass(), Base.class, IBase.class));
        assertEquals(String.class, genericTypeOf(new StaticBase<String>() {}.getClass(), StaticBase.class, IBase.class));
        assertEquals(String.class, genericTypeOf(new StaticIBase<String>() {}.getClass(), Base.class, StaticIBase.class));

        assertEquals(Integer.class, genericTypeOf(Mixed.class, Base.class, IBase.class));
        assertEquals(Integer.class, genericTypeOf(IIntImpl.class, Base.class, IBase.class));
        assertEquals(Object.class, genericTypeOf(INestedImpl.class, Base.class, IBase.class));
        assertEquals(Object.class, genericTypeOf(INonGeneric.class, IBase.class, INonGeneric.class));

        assertEquals(Integer.class, genericTypeOf(Deeper.class, Base.class, IBase.class));
        assertEquals(Integer.class, genericTypeOf(EvenDeeper.class, Base.class, IBase.class));
        assertEquals(Integer.class, genericTypeOf(Iface.class, Base.class, IBase.class));
        assertEquals(Integer.class, genericTypeOf(IDeeper.class, Base.class, IBase.class));
        assertEquals(Integer.class, genericTypeOf(Crazy.class, Base.class, IBase.class));
        assertEquals(Integer.class, genericTypeOf(Crazier.class, Base.class, IBase.class));
    }
}
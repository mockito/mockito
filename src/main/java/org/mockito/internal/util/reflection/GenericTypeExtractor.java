package org.mockito.internal.util.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Attempts to extract generic type of given target base class or target interface
 */
public class GenericTypeExtractor {

    /**
     * Extract generic type of root class either from the target base class or from target base interface.
     * Examples:
     *  <p>
     *  1. Foo implements IFoo[Integer]:
     *      genericTypeOf(Foo.class, Object.class, IFoo.class) returns Integer
     *  <p>
     *  2. Foo extends BaseFoo[String]:
     *      genericTypeOf(Foo.class, BaseFoo.class, IFoo.class) returns String
     *  <p>
     *  3. Foo extends BaseFoo; BaseFoo implements IFoo[String]:
     *      genericTypeOf(Foo.class, BaseFoo.class, Object.class) returns String
     *  <p>
     *  Does not support nested generics, only supports single type parameter.
     *
     * @param rootClass - the root class that the search begins from
     * @param targetBaseClass - if one of the classes in the root class' hierarchy extends this base class
     *                        it will be used for generic type extraction
     * @param targetBaseInterface - if one of the interfaces in the root class' hierarchy implements this interface
     *                            it will be used for generic type extraction
     * @return generic interface if found, Object.class if not found.
     */
    public static Class<?> genericTypeOf(Class<?> rootClass, Class<?> targetBaseClass, Class<?> targetBaseInterface) {
        //looking for candidates in the hierarchy of rootClass
        Class<?> match = rootClass;
        while(match != Object.class) {
            //check the super class first
            if (match.getSuperclass() == targetBaseClass) {
                return extractGeneric(match.getGenericSuperclass());
            }
            //check the interfaces (recursively)
            Type genericInterface = findGenericInteface(match, targetBaseInterface);
            if (genericInterface != null) {
                return extractGeneric(genericInterface);
            }
            //recurse the hierarchy
            match = match.getSuperclass();
        }
        return Object.class;
    }

    /**
     * Finds generic interface implementation based on the source class and the target interface.
     * Returns null if not found. Recurses the interface hierarchy.
     */
    private static Type findGenericInteface(Class<?> sourceClass, Class<?> targetBaseInterface) {
        for (int i = 0; i < sourceClass.getInterfaces().length; i++) {
            Class<?> inter = sourceClass.getInterfaces()[i];
            if (inter == targetBaseInterface) {
                return sourceClass.getGenericInterfaces()[0];
            } else {
                Type deeper = findGenericInteface(inter, targetBaseInterface);
                if (deeper != null) {
                    return deeper;
                }
            }
        }
        return null;
    }

    /**
     * Attempts to extract generic parameter type of given type.
     * If there is no generic parameter it returns Object.class
     */
    private static Class<?> extractGeneric(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
            if (genericTypes.length > 0 && genericTypes[0] instanceof Class) {
                return (Class<?>) genericTypes[0];
            }
        }
        return Object.class;
    }
}

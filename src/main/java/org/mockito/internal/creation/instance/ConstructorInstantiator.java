package org.mockito.internal.creation.instance;

import static org.mockito.internal.util.StringJoiner.join;
import java.lang.reflect.Constructor;
import org.mockito.internal.util.reflection.AccessibilityChanger;

public class ConstructorInstantiator implements Instantiator {

    private final Object outerClassInstance;

    public ConstructorInstantiator(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }

    public <T> T newInstance(Class<T> cls) {
        if (outerClassInstance == null) {
            return noArgConstructor(cls);
        }
        return withParams(cls, outerClassInstance);
    }

    private static <T> T withParams(Class<T> cls, Object... params) {
        try {
            //this is kind of over-engineered because we don't need to support more params
            //however, I know we will be needing it :)
            for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
                Class<?>[] types = constructor.getParameterTypes();
                if (paramsMatch(types, params)) {
                    return invokeConstructor(constructor, params);
                }
            }
        } catch (Exception e) {
            throw paramsException(cls, e);
        }
        throw paramsException(cls, null);
    }

    @SuppressWarnings("unchecked")
    private static <T> T invokeConstructor(Constructor<?> constructor, Object... params) throws java.lang.InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        AccessibilityChanger accessibility = new AccessibilityChanger();
        accessibility.enableAccess(constructor);
        return (T) constructor.newInstance(params);
    }

    private static <T> InstantiationException paramsException(Class<T> cls, Exception e) {
        return new InstantiationException(join(
                "Unable to create instance of '" + cls.getSimpleName() + "'.",
                "Please ensure that the outer instance has correct type and that the target class has 0-arg constructor.")
                , e);
    }

    private static boolean paramsMatch(Class<?>[] types, Object[] params) {
        if (params.length != types.length) {
            return false;
        }
        for (int i = 0; i < params.length; i++) {
            if (!types[i].isInstance(params[i])) {
                return false;
            }
        }
        return true;
    }

    private static <T> T noArgConstructor(Class<T> cls) {
        try {
            return invokeConstructor(cls.getDeclaredConstructor());
        } catch (Throwable t) {
            throw new InstantiationException(join(
                    "Unable to create instance of '" + cls.getSimpleName() + "'.",
                    "Please ensure it has 0-arg constructor which invokes cleanly."),
                    t);
        }
    }
}

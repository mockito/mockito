package org.mockito.internal.util.reflection;

import org.mockito.exceptions.Reporter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.Mockito.mock;

public class FieldConstructorInitializer {

    private final Field field;
    private final Object target;

    public FieldConstructorInitializer(Field field, Object target) {
        this.field = field;
        this.target = target;
    }


    public void initialize(Set<Object> mocks) {
        try {
            final Object instance = newInstance(mocks);
            final AccessibilityChanger changer = new AccessibilityChanger();
            changer.enableAccess(field);
            field.set(target, instance);
            changer.safelyDisableAccess(field);
        } catch (Exception e) {
            new Reporter().cannotInitializeForInjectMocksAnnotation(
                    field.getName(), e);
        }
    }

    private Object newInstance(Set<Object> mocks) throws Exception {
        final Constructor<?> c = biggestConstructor(field.getType());
        final Object[] args = args(c, mocks);
        final AccessibilityChanger changer = new AccessibilityChanger();
        changer.enableAccess(c);
        final Object instance = c.newInstance(args);
        changer.safelyDisableAccess(c);
        return instance;
    }

    private Object[] args(Constructor<?> c, Set<Object> mocks) {
        final List<Object> args = new ArrayList<Object>(c.getParameterTypes().length);

        for (Class<?> paramClass : c.getParameterTypes()) {
            args.add(findMockByTypeOrCreateOne(mocks, paramClass));
        }
        return args.toArray();
    }

    private Object findMockByTypeOrCreateOne(Set<Object> mocks, Class<?> paramClass) {
        for (Object mock : mocks) {
            if (paramClass.isAssignableFrom(mock.getClass())) {
                return mock;
            }
        }
        return notMockFoundForClass(paramClass);
    }

    private Object notMockFoundForClass(Class<?> paramClass) {
        return mock(paramClass);
    }

    private Constructor<?> biggestConstructor(Class<?> clazz) {
        final List<Constructor<?>> constructors = Arrays.asList(clazz.getConstructors());
        Collections.sort(constructors, new Comparator<Constructor<?>>() {

            public int compare(Constructor<?> o1, Constructor<?> o2) {
                return o2.getParameterTypes().length - o1.getParameterTypes().length;
            }
        });
        return constructors.get(0);
    }

}

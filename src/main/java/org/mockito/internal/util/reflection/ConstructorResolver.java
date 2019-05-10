/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

/**
 * Represents the strategy used to resolve a constructor and its arguments.
 */
public interface ConstructorResolver {

    /**
     * Indicates whether this resolver is able to resolve a constructor and its arguments, based on
     * type to resolve and resolver strategy.
     *
     * @return {@code true} if a constructor and its arguments are resolvable, {@code false}
     * otherwise.
     * @throws MockitoException implementation may choose to throw if contructor and/or its
     *                          arguments are not resolvable for some reason.
     */
    boolean isResolvable() throws MockitoException;

    /**
     * Resolves constructor.
     * <p>
     * If {@link #isResolvable()} is {@code true} this method must return a constructor.
     * </p>
     *
     * @return constructor, should not be null.
     * @throws MockitoException implementation can throw if contructor is not resolvable for some
     *                          reason (when {only @link #isResolvable()} is not {@code true}).
     */
    Constructor<?> resolveConstructor() throws MockitoException;

    /**
     * Resolves constructor arguments instances matching parameters of constructor resolved by
     * {@link #resolveConstructor()}.
     * <p>
     * If {@link #isResolvable()} is {@code true} this method must return correct arguments.
     * </p>
     *
     * @return array of argument instances to be given to the constructor, should not be null.
     * @throws MockitoException implementation can throw if arguments are not resolvable for some
     *                          reason (only when {@link #isResolvable()} is not {@code true}).
     */
    Object[] resolveArguments() throws MockitoException;

    /**
     * Resolves the no-arguments constructor (or default constructor) of the given type.
     * <p>
     * This resolver throws exception if given type has only parameterized constructors.
     * </p>
     */
    class NoArgsConstructorResolver implements ConstructorResolver {
        protected final Class<?> type;
        private Constructor<?> constructor;

        /**
         * Prepares Resolver to resolve constructor of given type.
         *
         * @param type class to resolve constructor
         */
        public NoArgsConstructorResolver(Class<?> type) {
            this.type = type;
        }

        /**
         * @return {@code true} if there is a no-arguments constructor, throws otherwise.
         * @throws MockitoException throws if given type has only parameterized constructors.
         */
        @Override
        public boolean isResolvable() throws MockitoException {
            return resolveConstructor() != null;
        }

        /**
         * @return the no-arguments constructor.
         * @throws MockitoException throws if given type has only parameterized constructors.
         */
        @Override
        public Constructor<?> resolveConstructor() throws MockitoException {
            if (constructor == null) {
                try {
                    constructor = type.getDeclaredConstructor();
                } catch (NoSuchMethodException e) {
                    throw new MockitoException("the type '" + type.getSimpleName() + "' has no default constructor", e);
                }
            }
            return constructor;
        }

        /**
         * @return an empty array matching no-arguments constructor.
         */
        @Override
        public Object[] resolveArguments() {
            return new Object[0];
        }
    }

    /**
     * Resolves the no-arguments constructor (or default constructor) of the given type.
     * <p>
     * {@link #isResolvable()} returning {@code false} indicates resolver cannot find no-arguments
     * constructor.
     * </p>
     */
    class LenientNoArgsConstructorResolver extends NoArgsConstructorResolver {

        /**
         * Prepares Resolver to resolve constructor of given type.
         *
         * @param type class to resolve constructor
         */
        public LenientNoArgsConstructorResolver(Class<?> type) {
            super(type);
        }

        /**
         * @return {@code true} if there is a no-arguments constructor, {@code false} otherwise.
         */
        @Override
        public boolean isResolvable() {
            try {
                return resolveConstructor() != null;
            } catch (MockitoException e) {
                return false;
            }
        }
    }

    /**
     * Resolves the constructor with the highest number of parameters and, in case of egality, the
     * highest number of mockable parameters.<br/>
     * Try to resolve actual constuctor arguments instances with given mocks matching constructor
     * parameters, or null if there is no mocks matching a parameter.
     *
     * <blockquote>
     * TODO on missing mock type, shall it abandon or create "noname" mocks.
     * TODO and what if the arg type is not mockable.
     * </blockquote>
     */
    class BiggestConstructorResolver implements ConstructorResolver {
        protected final Class<?> type;
        protected final Set<Object> mocks;

        protected final Comparator<Constructor<?>> byParameterNumber = new Comparator<Constructor<?>>() {
            public int compare(Constructor<?> constructorA, Constructor<?> constructorB) {
                int argLengths = constructorB.getParameterTypes().length - constructorA.getParameterTypes().length;
                if (argLengths == 0) {
                    int constructorAMockableParamsSize = countMockableParams(constructorA);
                    int constructorBMockableParamsSize = countMockableParams(constructorB);
                    return constructorBMockableParamsSize - constructorAMockableParamsSize;
                }
                return argLengths;
            }

            private int countMockableParams(Constructor<?> constructor) {
                int constructorMockableParamsSize = 0;
                for (Class<?> aClass : constructor.getParameterTypes()) {
                    if (MockUtil.typeMockabilityOf(aClass).mockable()) {
                        constructorMockableParamsSize++;
                    }
                }
                return constructorMockableParamsSize;
            }
        };

        private Constructor<?> constructor;
        private Object[] arguments;

        /**
         * Prepares Resolver to resolve constructor of given type and resolve constuctor arguments
         * with given mocks.
         *
         * @param type  class to resolve constructor
         * @param mocks set of mocks instances
         */
        public BiggestConstructorResolver(Class<?> type, Set<Object> mocks) {
            this.type = type;
            this.mocks = mocks;
        }

        /**
         * @return {@code true} if there is a parameterized constructor, throws otherwise.
         * @throws MockitoException throws if given type has only no-arguments constructor.
         */
        @Override
        public boolean isResolvable() throws MockitoException {
            return resolveConstructor() != null;
        }

        /**
         * @return constructor with the highest number of parameters and, in case of egality, the
         * highest number of mockable parameters.
         * @throws MockitoException throws if given type has only no-arguments constructor.
         */
        @Override
        public Constructor<?> resolveConstructor() throws MockitoException {
            if (constructor == null) {
                final List<? extends Constructor<?>> constructors = Arrays.asList(type.getDeclaredConstructors());
                Collections.sort(constructors, byParameterNumber);
                final Constructor<?> constructor = constructors.get(0);
                checkParameterized(constructor);
                this.constructor = constructor;
            }
            return constructor;
        }

        /**
         * @return an array of actual argument instances of given mocks matching constructor
         * parameters, or null if there is no mocks matching a parameter.
         */
        @Override
        public Object[] resolveArguments() {
            if (arguments == null) {
                final Class<?>[] argTypes = resolveConstructor().getParameterTypes();
                final List<Object> argumentInstances = new ArrayList<Object>(argTypes.length);
                for (Class<?> argType : argTypes) {
                    argumentInstances.add(objectThatIsAssignableFrom(argType));
                }
                arguments = argumentInstances.toArray();
            }
            return arguments;
        }

        protected Object objectThatIsAssignableFrom(Class<?> argType) {
            for (Object object : mocks) {
                if (object != null && argType.isAssignableFrom(object.getClass())) {
                    return object;
                }
            }
            return null;
        }

        protected void checkParameterized(Constructor<?> constructor) {
            if (constructor.getParameterTypes().length == 0) {
                throw new MockitoException("the type '" + type.getSimpleName() + "' has no parameterized constructor");
            }
        }
    }

    /**
     * Resolves the constructor with the highest number of parameters and, in case of egality, the
     * highest number of mockable parameters.<br/>
     * Resolves actual constructor arguments instances with given mocks matching constructor
     * parameters, or indicates there is no resolvable constructors if there is no mocks matching a
     * parameter.
     */
    class StrictBiggestConstructorResolver extends BiggestConstructorResolver {

        private boolean hasNullArguments = false;

        /**
         * Prepares Resolver to resolve constructor of given type and resolve constuctor arguments
         * with given mocks.
         *
         * @param type  class to resolve constructor
         * @param mocks set of mocks instances
         */
        public StrictBiggestConstructorResolver(Class<?> type, Set<Object> mocks) {
            super(type, mocks);
        }

        /**
         * @return {@code true} if given mocks matches all arguments of constructor with the highest
         * number of parameters, {@code false} otherwise.
         * @throws MockitoException throws if given type has only no-arguments constructor.
         */
        @Override
        public boolean isResolvable() throws MockitoException {
            resolveArguments();
            return !hasNullArguments;
        }

        @Override
        protected Object objectThatIsAssignableFrom(Class<?> argType) {
            final Object object = super.objectThatIsAssignableFrom(argType);
            if (object == null) {
                hasNullArguments = true;
            }
            return object;
        }
    }

}

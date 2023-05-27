package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

public class MatchParameterizedClass extends MatchClass implements MatchType {

    private final List<Type> resolvedTypes;

    protected MatchParameterizedClass(Class<?> clazz, List<Type> resolvedTypes) {
        super(clazz);
        this.resolvedTypes = resolvedTypes;
    }

    @Override
    public boolean matches(MatchType other) {
        return super.matches(other)
            && (other instanceof MatchParameterizedClass && resolvedTypesMatch((MatchParameterizedClass) other)
            || matchesSuperTypeOfOther(other));
    }

    private boolean matchesSuperTypeOfOther(MatchType other) {
        if (other instanceof HasClass) {
            Class<?> clazz = ((HasClass) other).getTheClass();
            Type genericSuperclass = clazz.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Optional<MatchType> matchTypeSuper = ofParameterizedType((ParameterizedType) genericSuperclass);
                if (matchTypeSuper.isPresent()) {
                    return this.matches(matchTypeSuper.get());
                }
            }
        }
        return false;
    }

    private boolean resolvedTypesMatch(MatchParameterizedClass other) {
        if (resolvedTypes.equals(other.resolvedTypes)) {
            return true;
        }
        Iterator<Type> targetIterator = this.resolvedTypes.iterator();
        Iterator<Type> sourceIterator = other.resolvedTypes.iterator();
        boolean typesMatch = true;
        while (targetIterator.hasNext() && sourceIterator.hasNext() && typesMatch) {
            Type nextTarget = targetIterator.next();
            Type nextSource = sourceIterator.next();
            if (nextTarget instanceof MatchType && nextSource instanceof MatchType) {
                typesMatch = ((MatchType) nextTarget).matches((MatchType) nextSource);
            }
            else if (nextTarget instanceof MatchWildcard) {
                typesMatch = ((MatchWildcard) nextTarget).matches(nextSource);
            }
            else if (nextSource instanceof MatchWildcard) {
                typesMatch = ((MatchWildcard) nextSource).targetMatches(nextTarget);
            }
            else if (nextTarget instanceof TypeVariable && nextSource instanceof Class) {
                typesMatch = checkBounds((TypeVariable<?>) nextTarget, (Class<?>) nextSource);
            }
            else if (nextTarget instanceof Class && nextSource instanceof Class) {
                typesMatch = nextTarget.equals(nextSource);
            }
            else {
                typesMatch = false;
            }
        }
        return typesMatch && !targetIterator.hasNext() && !sourceIterator.hasNext();
    }

    private boolean checkBounds(TypeVariable<?> target, Class<?> source) {
        Type[] bounds = target.getBounds();
        if (bounds.length > 0) {
            boolean success = true;
            for (Type bound : bounds) {
                success = bound instanceof Class && ((Class<?>) bound).isAssignableFrom(source);
                if (!success) {
                    break;
                }
            }
            return success;
        }
        return false;
    }

    public VariableResolver toResolver() {
        Map<TypeVariable<?>, Type> argumentMap = new HashMap<>();
        TypeVariable<? extends Class<?>>[] parameters = this.getTheClass().getTypeParameters();
        for (int i = 0; i < parameters.length; i++) {
            TypeVariable<?> parameter = parameters[i];
            Type resolvedType = this.resolvedTypes.get(i);
            argumentMap.put(parameter, resolvedType);
        }
        return typeVariable -> Optional.of(argumentMap.get(typeVariable));
    }

    static MatchType ofClassAndResolver(Class<?> clazz, VariableResolver resolver) {
        TypeVariable<?>[] parameters = clazz.getTypeParameters();
        List<Type> resolvedTypes = new ArrayList<>(parameters.length);
        for (TypeVariable<?> parameter : parameters) {
            Optional<Type> optionalResolved = resolver.resolve(parameter);
            optionalResolved.ifPresent(resolvedTypes::add);
        }
        return new MatchParameterizedClass(clazz, resolvedTypes);
    }

    static Optional<MatchType> ofParameterizedType(ParameterizedType parameterizedType) {
        if (parameterizedType.getRawType() instanceof Class<?>) {
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            List<Type> resolvedTypes = Arrays.asList(typeArguments);
            return Optional.of(new MatchParameterizedClass((Class<?>) parameterizedType.getRawType(), resolvedTypes));
        }
        else {
            return Optional.empty();
        }
    }
}

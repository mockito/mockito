package org.mockito.internal.util.reflection;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * Type representing bounds of a type variable, allows to keep all bounds information.
 *
 * <p>It uses the first bound in the array, as this array is never null and always contains at least
 * one element (Object is always here if no bounds are declared).</p>
 *
 * <p>If upper bounds are declared with SomeClass and additional interfaces, then firstBound will be SomeClass and
 * interfacesBound will be an array of the additional interfaces.
 *
 * i.e. <code>SomeClass</code>.
 * <pre class="code"><code class="java">
 *     interface UpperBoundedTypeWithClass<E extends Comparable<E> & Cloneable> {
 *         E get();
 *     }
 *     // will return Comparable type
 * </code></pre>
 * </p>
 */
public class BoundedType implements Type {
    private TypeVariable typeVariable;


    public BoundedType(TypeVariable typeVariable) {
        this.typeVariable = typeVariable;
    }

    /**
     * @return either a class or an interface (parameterized or not), if no bounds declared Object is returned.
     */
    public Type firstBound() {
        return typeVariable.getBounds()[0]; //
    }

    /**
     * On a Type Variable (typeVar extends AClass_0 & I_1 & I_2 & etc), will return an array
     * containing I_1 and I_2.
     *
     * @return other bounds for this type, these bounds can only be only interfaces as the JLS says,
     * empty array if no other bound declared.
     */
    public Type[] interfaceBounds() {
        Type[] interfaceBounds = new Type[typeVariable.getBounds().length - 1];
        System.arraycopy(typeVariable.getBounds(), 1, interfaceBounds, 0, typeVariable.getBounds().length - 1);
        return interfaceBounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return typeVariable.equals(((BoundedType) o).typeVariable);

    }

    @Override
    public int hashCode() {
        return typeVariable.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{firstBound=").append(firstBound());
        sb.append(", interfaceBounds=").append(Arrays.deepToString(interfaceBounds()));
        sb.append('}');
        return sb.toString();
    }
}

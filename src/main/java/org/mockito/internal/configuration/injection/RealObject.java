package org.mockito.internal.configuration.injection;

/**
 * Created by tim on 2-8-15.
 */
public class RealObject {

    private final Object value;

    private final String name;

    public RealObject(Object value, String name) {
        this.value = value;
        this.name = name;
    }

    public Object getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}

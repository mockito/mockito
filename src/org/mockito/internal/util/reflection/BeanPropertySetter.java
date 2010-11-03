package org.mockito.internal.util.reflection;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This utility class will call the setter of the property to inject a new value.
 */
public class BeanPropertySetter {

    private final Object target;
    private boolean reportNoSetterFound;
    private final Field field;

    /**
     * New BeanPropertySetter
     * @param target The target on which the setter must be invoked
     * @param propertyField The field that should be accessed with the setter
     * @param reportNoSetterFound Allow the set method to raise an Exception if the setter cannot be found
     */
    public BeanPropertySetter(final Object target, final Field propertyField, boolean reportNoSetterFound) {
        this.field = propertyField;
        this.target = target;
        this.reportNoSetterFound = reportNoSetterFound;
    }

    /**
     * New BeanPropertySetter that don't report failure
     * @param target The target on which the setter must be invoked
     * @param propertyField The propertyField that must be accessed through a setter
     */
    public BeanPropertySetter(final Object target, final Field propertyField) {
        this(target, propertyField, false);
    }

    /**
     * Set the value to the property represented by this {@link BeanPropertySetter}
     * @param value the new value to pass to the property setter
     * @return <code>true</code> if the value has been injected, <code>false</code> otherwise
     * @throws RuntimeException Can be thrown if the setter threw an exception, if the setter is not accessible
     *          or, if <code>reportNoSetterFound</code> and setter could not be found.
     */
    public boolean set(final Object value) {

        AccessibilityChanger changer = new AccessibilityChanger();
        Method writeMethod = null;
        try {
            BeanInfo targetInfo = Introspector.getBeanInfo(target.getClass());
            PropertyDescriptor[] propertyDescriptors = targetInfo.getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if(propertyNameMatchFieldName(propertyDescriptor)
                        && propertyTypeMatchFieldType(propertyDescriptor)) {
                    writeMethod = propertyDescriptor.getWriteMethod();
                    if(writeMethod != null) {
                        changer.enableAccess(writeMethod);
                        writeMethod.invoke(target, value);
                        return true;
                    }
                }
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Problems setting value:[" + value + "] on object: [" + target + "] the setter of property [" + field.getName() + "] threw an exception", e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Problems setting value:[" + value + "] on object: [" + target + "] the setter of property [" + field.getName() + "] is not accessible", e);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Problems setting value: [" + value + "] on object: [" + target + "] for property : [" + field.getName() + "], cannot introspect " + target.getClass(), e);
        } finally {
            if(writeMethod != null) {
                changer.safelyDisableAccess(writeMethod);
            }
        }

        reportNoSetterFound();
        return false;
    }

    private void reportNoSetterFound() {
        if(reportNoSetterFound) {
            throw new RuntimeException("Problems setting value on object: [" + target + "] for property : [" + field.getName() + "], setter not found");
        }
    }

    private boolean propertyTypeMatchFieldType(PropertyDescriptor pd) {
        return field.getType().equals(pd.getPropertyType());
    }

    private boolean propertyNameMatchFieldName(PropertyDescriptor pd) {
        return field.getName().equals(pd.getName());
    }

}

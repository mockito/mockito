package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Inject mocks using first setters then fields, if no setters available.
 *
 * <p>
 * <u>Algorithm :<br></u>
 * for each field annotated by @InjectMocks
 *   <ul>
 *   <li>copy mocks set
 *   <li>initialize field annotated by @InjectMocks
 *   <li>for each field in @InjectMocks type ordered from sub-type to super-type
 *     <ul>
 *     <li>find mock candidate by type
 *     <li>if more than *one* candidate find mock candidate on name
 *     <li>if one mock candidate then
 *       <ul>
 *       <li>set mock by property setter if possible
 *       <li>else set mock by field injection
 *       </ul>
 *     <li>remove mock from mocks copy (mocks are just injected once)
 *     <li>else don't fail, user will then provide dependencies
 *     </ul>
 *   </ul>
 * </p>
 *
 * <p>
 * <u>Note:</u> If the field needing injection is not initialized, the strategy tries
 * to create one using a no-arg constructor of the field type.
 * </p>
 */
public class PropertyAndSetterInjection extends MockInjectionStrategy {

    private final MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    private Comparator<Field> supertypesLast = new Comparator<Field>() {
        public int compare(Field field1, Field field2) {
            Class<?> field1Type = field1.getType();
            Class<?> field2Type = field2.getType();

            if(field1Type.isAssignableFrom(field2Type)) {
                return 1;
            }
            if(field2Type.isAssignableFrom(field1Type)) {
                return -1;
            }
            return 0;
        }
    };


    public boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        Set<Object> mocksToBeInjected = new HashSet<Object>(mockCandidates);
        Object fieldInstanceNeedingInjection = null;
        try {
            fieldInstanceNeedingInjection = new FieldInitializer(fieldOwner, field).initialize();
        } catch (MockitoException e) {
            new Reporter().cannotInitializeForInjectMocksAnnotation(field.getName(), e);
        }

        // for each field in the class hierarchy
        Class<?> fieldClass = fieldInstanceNeedingInjection.getClass();
        while (fieldClass != Object.class) {
            injectMockCandidate(fieldClass, mocksToBeInjected, fieldInstanceNeedingInjection);
            fieldClass = fieldClass.getSuperclass();
        }
        return false;
    }


    private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object instance) {
        for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
            Object injected = mockCandidateFilter.filterCandidate(mocks, field, instance).thenInject();
            mocks.remove(injected);
        }
    }

    private Field[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        Field[] declaredFields = awaitingInjectionClazz.getDeclaredFields();
        Arrays.sort(declaredFields, supertypesLast);
        return declaredFields;
    }

}

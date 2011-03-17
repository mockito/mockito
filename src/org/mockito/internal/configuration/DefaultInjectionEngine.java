/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.internal.configuration.injection.MockInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Set;

/**
 * Initializes mock/spies dependencies for fields annotated with &#064;InjectMocks
 * <p/>
 * See {@link org.mockito.MockitoAnnotations}
 */
public class DefaultInjectionEngine {

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


    public void injectMocksOnFields(Set<Field> needingInjection, Set<Object> mocks, Object testClassInstance) {
        MockInjection.onFields(needingInjection, testClassInstance)
                .withMocks(mocks)
                .tryConstructorInjection()
                .tryPropertyOrFieldInjection()
                .apply();
    }



    /*
        XXX         Attention en fait le SpyAnnotationEngine peut tenter d'instancier le field
                    annoté par @InjectMocks

        Solution :  Dans SpyAnnotationEngine, ne pas essayer d'instancier un field annoté par @InjectMocks
        Drawbacks : il faut gérer de manière particulière le @Spy depuis DefaultInjectionEngine
     */

    /*
        XXX :   Autre soucis qui arrive avec l'injection par constructeur de @InjectMocks
                il faut pte prévoir le nettoyage des instances
     */


}

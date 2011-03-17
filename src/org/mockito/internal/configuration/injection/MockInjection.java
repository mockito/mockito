package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.internal.util.Checks.checkItemsNotNull;
import static org.mockito.internal.util.Checks.checkNotNull;

/**
 * Internal injection configuration utility.
 *
 * <p>
 * Allow the user of this class to configure the way the injection of mocks will happen.
 * </p>
 *
 */
public class MockInjection {

    /**
     * Create a new configuration setup for a field
     *
     *
     * @param field Field needing mock injection
     * @param ofInstance Instance owning the <code>field</code>
     * @return New configuration builder
     */
    public static OngoingMockInjection onField(Field field, Object ofInstance) {
        return new OngoingMockInjection(field, ofInstance);
    }

    /**
     * Create a new configuration setup for fields
     *
     *
     * @param fields Fields needing mock injection
     * @param ofInstance Instance owning the <code>field</code>
     * @return New configuration builder
     */
    public static OngoingMockInjection onFields(Set<Field> fields, Object ofInstance) {
        return new OngoingMockInjection(fields, ofInstance);
    }

    public static class OngoingMockInjection {
        private Set<Field> fields = new HashSet<Field>();
        private Set<Object> mocks = new HashSet<Object>();
        private Object fieldOwner;
        private MockInjectionStrategy injectionStrategies;

        private OngoingMockInjection(Field field, Object fieldOwner) {
            this(Collections.singleton(field), fieldOwner);
        }

        private OngoingMockInjection(Set<Field> fields, Object fieldOwner) {
            this.fieldOwner = checkNotNull(fieldOwner, "fieldOwner");
            this.fields.addAll(checkItemsNotNull(fields, "fields"));
        }

        public OngoingMockInjection withMocks(Set<Object> mocks) {
            this.mocks.addAll(checkNotNull(mocks, "mocks"));
            return this;
        }

        public OngoingMockInjection tryConstructorInjection() {
            appendStrategy(new ConstructorInjection());
            return this;
        }

        public OngoingMockInjection tryPropertyOrFieldInjection() {
            appendStrategy(new PropertyAndSetterInjection());
            return this;
        }

        private void appendStrategy(MockInjectionStrategy strategy) {
            if(injectionStrategies == null) {
                injectionStrategies = strategy;
            } else {
                injectionStrategies.thenTry(strategy);
            }

        }

        public void apply() {
            for (Field field : fields) {
                injectionStrategies.process(field, fieldOwner, mocks);
            }
        }
    }
}

package org.mockito.internal.creation.bytebuddy;

import org.mockito.internal.creation.bytebuddy.access.MockAccess;
import java.lang.instrument.Instrumentation;
import java.util.Collections;

abstract class ModuleOpener {

    abstract void adjust(Class<?> type);

    static  ModuleOpener make() {
        return new Default();
    }

    static class Default extends ModuleOpener {

        @Override
        void adjust(Class<?> type) {
            if (!MockAccess.class.getModule().isOpen(MockAccess.class.getPackageName(), type.getModule())) {
                MockAccess.class.getModule().addOpens(MockAccess.class.getPackageName(), type.getModule());
            }
        }
    }

    static class InstrumentationBased extends Default {

        private final Instrumentation instrumentation;

        InstrumentationBased(Instrumentation instrumentation) {
            this.instrumentation = instrumentation;
        }

        @Override
        void adjust(Class<?> type) {
            super.adjust(type);
            String packageName = type.getPackageName();
            if (packageName.isEmpty()) {
                return;
            }
            if (instrumentation != null && !type.getModule().isOpen(packageName, MockAccess.class.getModule())) {
                instrumentation.redefineModule(type.getModule(),
                    Collections.emptySet(),
                    Collections.emptyMap(),
                    Collections.singletonMap(packageName, Collections.singleton(MockAccess.class.getModule())),
                    Collections.emptySet(),
                    Collections.emptyMap());
            }
        }
    }
}

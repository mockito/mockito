package org.mockito.rules;

import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.NoOp;

public final class CGLibObjectCreationEngineImpl implements ObjectCreationEngine {

    private static class Holder {
        public static final CGLibObjectCreationEngineImpl INSTANCE = new CGLibObjectCreationEngineImpl();
    }

    public static CGLibObjectCreationEngineImpl getInstance() {
        return Holder.INSTANCE;
    }

    private CGLibObjectCreationEngineImpl() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(final Class<T> type) {
        // Using CGLIB instead of e.g. type.newInstance() allows to
        // construct objects with package scope constructors, yet maybe it is a
        // bit of a overkill?
        final Enhancer enhancer = new Enhancer();
        enhancer.setCallback(NoOp.INSTANCE);
        enhancer.setSuperclass(type);
        return (T) enhancer.create();
    }

}

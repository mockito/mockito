package org.mockito.internal.util.reflection;

import org.mockito.plugins.MemberAccessor;

public class InstrumentationMemberAccessorFactory implements MemberAccessorFactory {
    @Override
    public MemberAccessor create() {
        return new InstrumentationMemberAccessor();
    }
}

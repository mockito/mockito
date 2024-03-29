package org.mockito.internal.util.reflection;

import org.mockito.plugins.MemberAccessor;

public class ReflectionMemberAccessorFactory implements MemberAccessorFactory {
    @Override
    public MemberAccessor create() {
        return new ReflectionMemberAccessor();
    }
}

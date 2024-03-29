package org.mockito.internal.util.reflection;

import org.mockito.plugins.MemberAccessor;

public interface MemberAccessorFactory {
    MemberAccessor create();
}

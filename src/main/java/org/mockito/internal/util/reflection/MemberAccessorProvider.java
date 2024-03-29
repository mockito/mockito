package org.mockito.internal.util.reflection;

import org.mockito.plugins.MemberAccessor;
import org.mockito.internal.configuration.plugins.Plugins;

public class MemberAccessorProvider {
    private static final MemberAccessor accessor = Plugins.getMemberAccessor();

    public static MemberAccessor getAccessor() {
        return accessor;
    }
}

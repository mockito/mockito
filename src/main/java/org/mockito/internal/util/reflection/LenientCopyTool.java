package org.mockito.internal.util.reflection;

import org.mockito.internal.configuration.plugins.Plugins;

public class LenientCopyTool {

    private final FieldCopier fieldCopier = new FieldCopier(Plugins.getMemberAccessor());

    public <T> void copyToMock(T from, T mock) {
        fieldCopier.copyFields(from, mock);
    }

    public <T> void copyToRealObject(T from, T to) {
        fieldCopier.copyFields(from, to);
    }
}

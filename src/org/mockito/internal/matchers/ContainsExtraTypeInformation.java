package org.mockito.internal.matchers;

import org.hamcrest.SelfDescribing;

public interface ContainsExtraTypeInformation {
    SelfDescribing withExtraTypeInfo();

    boolean typeMatches(Object object);
}
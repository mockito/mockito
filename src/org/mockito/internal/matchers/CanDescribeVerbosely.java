package org.mockito.internal.matchers;

import org.hamcrest.SelfDescribing;

public interface CanDescribeVerbosely {
    SelfDescribing getSelfDescribingVerbosely();

    boolean typeMatches(Object object);
}
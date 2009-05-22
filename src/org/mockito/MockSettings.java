package org.mockito;

import org.mockito.stubbing.Answer;

public interface MockSettings {
    
    MockSettings extraInterfaces(Class<?>... interfaces);

    MockSettings name(String name);

    //TODO: hide spiedInstance?
    MockSettings spiedInstance(Object object);

    //TODO: check out types when using this method - can we suppress deprecation warnings?
    MockSettings defaultAnswer(Answer defaultAnswer);
    
}
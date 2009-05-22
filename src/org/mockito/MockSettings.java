package org.mockito;

import org.mockito.stubbing.Answer;

public interface MockSettings {
    
    MockSettings extraInterfaces(Class<?>... interfaces);

    MockSettings name(String name);

    MockSettings spiedInstance(Object object);

    MockSettings defaultAnswer(Answer defaultAnswer);
    
}
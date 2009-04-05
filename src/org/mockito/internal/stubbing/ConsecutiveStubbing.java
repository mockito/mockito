/**
 * 
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.progress.DeprecatedOngoingStubbing;
import org.mockito.internal.progress.NewOngoingStubbing;
import org.mockito.stubbing.Answer;

public class ConsecutiveStubbing<T> extends BaseStubbing<T> {
    
    private final MockitoStubber mockitoStubber;

    public ConsecutiveStubbing(MockitoStubber mockitoStubber) {
        this.mockitoStubber = mockitoStubber;
    }

    public NewOngoingStubbing<T> thenAnswer(Answer<?> answer) {
        mockitoStubber.addConsecutiveAnswer(answer);
        return this;
    }
    
    public DeprecatedOngoingStubbing<T> toAnswer(Answer<?> answer) {
        mockitoStubber.addConsecutiveAnswer(answer);
        return this;
    }
}
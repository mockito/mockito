package org.mockito.internal.verification.within;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;
import org.mockito.mock.MockCreationSettings;
import org.mockito.verification.VerificationMode;

public class WithinVerfication implements VerificationMode, VerificationInOrderMode {

    private final long deadlineNanos;
    private final VerificationStrategy strategy;

    public WithinVerfication(long deadlineNanos, VerificationStrategy s) {
        this.deadlineNanos = deadlineNanos;
        this.strategy = s;
    }

    @Override
    public VerificationMode description(String description) {
        return this;
    }

    @Override
    public void verifyInOrder(VerificationDataInOrder data) {
        verify(data.getWanted(), data.getAllInvocations(), new InternalInOrderContext(data.getOrderingContext()));
    }

    @Override
    public void verify(VerificationData data) {
        verify(data.getTarget(), data.getAllInvocations(), null);
    }

    private void verify(MatchableInvocation wanted, List<Invocation> allInvocations, InternalInOrderContext orderingContext) {
        Object mock = wanted.getInvocation().getMock();

        BlockingDeque<Invocation> allInvocationsQueue = new LinkedBlockingDeque<Invocation>(allInvocations);

        InternInvocationListener listener = new InternInvocationListener(allInvocationsQueue);

        Collection<InvocationListener> invocationListeners = getInvocationListeners(mock);
        invocationListeners.add(listener);

        try {
            if (orderingContext == null) {
                processInvocations(wanted, allInvocationsQueue);
            } else {
                processInvocationsInOrder(wanted, allInvocationsQueue, orderingContext);
            }

        } finally {
            invocationListeners.remove(listener);
        }
    }

    private void processInvocationsInOrder(MatchableInvocation wanted, BlockingDeque<Invocation> allInvocationsQueue, InternalInOrderContext orderingContext) {

        Invocation invocation;

        // eat all not matching invocations
        while (true) {
            invocation = poll(allInvocationsQueue);
            if (!orderingContext.isVerified(invocation) && wanted.matches(invocation)) {
                break;
            }
            orderingContext.markVerified(invocation);
        }

        //

        BlockingDeque<Invocation> chunk; // = new
                                         // LinkedBlockingDeque<Invocation>(allInvocationsQueue);

        chunk = allInvocationsQueue;
        chunk.addFirst(invocation);

        processInvocations(wanted, chunk);

    }

    private void processInvocations(MatchableInvocation wanted, BlockingQueue<Invocation> allInvocationsQueue) {
        while (true) {
            Invocation invocation = poll(allInvocationsQueue);

            if (invocation == null) {
                break; // the time elapsed
            }
            switch (verify(wanted, invocation)) {
                case FINISHED_SUCCESSFULL:
                    return;
                case GIVE_ME_THE_NEXT_INVOCATION:
                    continue;
            }
        }

        strategy.verifyAfterTimeElapsed(wanted);
    }

    private VerificationResult verify(MatchableInvocation wanted, Invocation invocation) {
        if (!wanted.matches(invocation)) {
            return strategy.verifyNotMatchingInvocation(invocation, wanted);
        }

        invocation.markVerified();
        wanted.captureArgumentsFrom(invocation);

        return strategy.verifyMatchingInvocation(invocation, wanted);
    }

    private Invocation poll(BlockingQueue<Invocation> queue) {
        long remainingNanos = deadlineNanos - System.nanoTime();
        try {
            return queue.poll(remainingNanos, NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<InvocationListener> getInvocationListeners(final Object mock) {
        MockingDetails mockingDetails = Mockito.mockingDetails(mock);
        MockCreationSettings<?> mockCreationSettings = mockingDetails.getMockCreationSettings();
        Collection<InvocationListener> invocationListeners = mockCreationSettings.getInvocationListeners();
        return invocationListeners;
    }

    private final static class InternInvocationListener implements InvocationListener {

        private final BlockingQueue<Invocation> allInvocations;

        InternInvocationListener(BlockingQueue<Invocation> allInvocations) {
            this.allInvocations = allInvocations;
        }

        @Override
        public void reportInvocation(MethodInvocationReport report) {
            allInvocations.add((Invocation) report.getInvocation());
        }
    }
}

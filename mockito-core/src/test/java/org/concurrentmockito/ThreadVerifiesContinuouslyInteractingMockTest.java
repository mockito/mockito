import java.util.concurrent.locks.ReentrantLock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ThreadVerifiesContinuouslyInteractingMockTest extends TestBase {

    @Mock private IMethods mock;
    private final ReentrantLock lock = new ReentrantLock();

    @Test
    public void shouldAllowVerifyingInThreads() throws Exception {
        for (int i = 0; i < 100; i++) {
            performTest();
        }
    }

    private void performTest() throws InterruptedException {
        lock.lock();
        try {
            mock.simpleMethod();
        } finally {
            lock.unlock();
        }

        final Thread[] listeners = new Thread[2];
        for (int i = 0; i < listeners.length; i++) {
            final int x = i;
            listeners[i] = new Thread(() -> {
                try {
                    Thread.sleep(x * 10);
                    lock.lock();
                    try {
                        mock.simpleMethod();
                    } finally {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            listeners[i].start();
        }

        lock.lock();
        try {
            verify(mock, atLeastOnce()).simpleMethod();
        } finally {
            lock.unlock();
        }

        for (Thread listener : listeners) {
            listener.join();
        }
    }
}

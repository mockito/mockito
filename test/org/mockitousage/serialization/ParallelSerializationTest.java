package org.mockitousage.serialization;

import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.SimplePerRealmReloadingClassLoader;
import org.mockitoutil.SimpleSerializationUtil;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class ParallelSerializationTest {

    ExecutorService executorService = Executors.newFixedThreadPool(200);


    @Test
    public void single_mock_being_serialized_and_deserialized_in_different_classloaders_by_multiple_threads() throws ExecutionException, InterruptedException {
        final IMethods iMethods = mock(IMethods.class, withSettings().serializable());

        int iterations = 1;
        int threadingFactor = 100;

        for (int i = 0; i <= iterations; i++) {
            List<Future> futures = new ArrayList<Future>(threadingFactor);
            final CyclicBarrier barrier = new CyclicBarrier(threadingFactor);

            for (int j = 0; j < threadingFactor; j++) {
                final int finalJ = j;
                futures.add(executorService.submit(new Callable<Object>() {
                    public Object call() throws Exception {
                        System.out.println("[" + finalJ + "] waiting here");
                        barrier.await();
                        System.out.println("[" + finalJ + "] serializing now");
                        iMethods.arrayReturningMethod();
                        return read_stream_and_deserialize_it_in_class_loader_B(SimpleSerializationUtil.serializeMock(iMethods).toByteArray());
                    }
                }));
                executorService.submit(new Callable<Object>() {
                    public Object call() throws Exception {
                        barrier.await();
                        return iMethods.longObjectReturningMethod();
                    }
                });
            }
            for (Future future : futures) {
                future.get();
            }
        }
    }



    private Object read_stream_and_deserialize_it_in_class_loader_B(byte[] bytes) throws Exception {
        return new SimplePerRealmReloadingClassLoader(this.getClass().getClassLoader(), isolating_test_classes())
                .doInRealm(
                        "org.mockitousage.serialization.AcrossClassLoaderSerializationTest$ReadStreamAndDeserializeIt",
                        new Class[]{ byte[].class },
                        new Object[]{ bytes }
                );
    }


    private SimplePerRealmReloadingClassLoader.ReloadClassPredicate isolating_test_classes() {
        return new SimplePerRealmReloadingClassLoader.ReloadClassPredicate() {
            public boolean acceptReloadOf(String qualifiedName) {
                return qualifiedName.contains("org.mockitousage")
                        || qualifiedName.contains("org.mockitoutil");
            }
        };
    }


    // see read_stream_and_deserialize_it_in_class_loader_B
    public static class ReadStreamAndDeserializeIt implements Callable<Object> {
        private byte[] bytes;

        public ReadStreamAndDeserializeIt(byte[] bytes) {
            this.bytes = bytes;
        }

        public Object call() throws Exception {
            ByteArrayInputStream unserialize = new ByteArrayInputStream(bytes);
            return SimpleSerializationUtil.deserializeMock(unserialize, IMethods.class);
        }
    }
}

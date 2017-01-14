package mockitointegration;

import org.mockito.Mockito;
import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class MockitoMockBenchmark {

    private Class<?> clazz;

    private IMethodsClassLoader loader;

    public MockitoMockBenchmark() {
        loader = new IMethodsClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
    }

    @Setup(Level.Invocation)
    public void setup() throws ClassNotFoundException {
        clazz = loader.loadIMethods();
    }

    @TearDown
    public void teardown() {
        // Intentionally left blank
        // Else jmh annotation processor complains this method does not exist.
    }

    @Benchmark
    public void simple_mock() {
        Mockito.mock(clazz);
    }
}

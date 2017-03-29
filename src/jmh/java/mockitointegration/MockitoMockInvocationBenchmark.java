package mockitointegration;

import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class MockitoMockInvocationBenchmark {

    private IMethods mock;

    private IMethodsClassLoader loader;

    public MockitoMockInvocationBenchmark() {
        loader = new IMethodsClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
    }

    @Setup(Level.Invocation)
    public void setup() throws ClassNotFoundException {
        mock = Mockito.mock(loader.loadIMethods());
    }

    @TearDown
    public void teardown() {
        // Intentionally left blank
        // Else jmh annotation processor complains this method does not exist.
    }

    @Benchmark
    public void simple_mock() {
        mock.twoArgumentMethod(5, 7);
    }
}

package mockitointegration;

import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.openjdk.jmh.annotations.Benchmark;

public class MockitoBenchmarkTest {

    @Benchmark
    public void simple_mock() {
        Mockito.mock(IMethods.class);
    }
}

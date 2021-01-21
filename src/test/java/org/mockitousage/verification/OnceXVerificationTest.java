package org.mockitousage.verification;

import static org.mockito.Mockito.once;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.List;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

/**
 * @author shubhendu
 * @since 21/01/21
 */
public class OnceXVerificationTest extends TestBase {
    @Mock
    private List<String> mock;

    @Test
    public void shouldVerifyOnce() throws Exception {
        mock.clear();
        verify(mock, once()).clear();
    }

    @Test
    public void shouldFailVerification() throws Exception {
        mock.clear();
        mock.clear();
        verify(mock, times(2)).clear();
    }
}

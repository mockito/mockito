package org.mockitousage.androidtest

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.withSettings
import org.mockito.internal.creation.proxy.ProxyMockMaker

@RunWith(AndroidJUnit4::class)
class AaaMustRunFirstLocationTests {

    /**
     *  Regression test for https://github.com/mockito/mockito/issues/3171 and
     *  https://github.com/linkedin/dexmaker/issues/190.
     *
     *  Bug only triggers if the first time LocationImpl is used is with ProxyMockMaker, therefore
     *  this test must be the first to run.
     */
    @Test
    fun mockAndUseInterfaceWithProxyMockMaker() {
        val basicInterface = mock(BasicInterface::class.java,
            withSettings().mockMaker(ProxyMockMaker::class.java.name))
        basicInterface.interfaceMethod()
    }
}

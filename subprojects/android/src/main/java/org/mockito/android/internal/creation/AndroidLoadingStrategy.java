package org.mockito.android.internal.creation;

import static org.mockito.internal.util.StringUtil.join;

import java.io.File;
import net.bytebuddy.android.AndroidClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.SubclassLoader;

class AndroidLoadingStrategy implements SubclassLoader {

    @Override
    public ClassLoadingStrategy<ClassLoader> getStrategy(Class<?> mockFeatures) {
        File target = AndroidTempFileLocator.target;
        if (target == null) {
            throw new MockitoException(join(
                    "Could not look up implicit location for storing generated classes",
                    "",
                    "You can configure an explicit location by setting the system property",
                    "'org.mockito.android.target' to a folder for storing generated class files",
                    "This location must be in private scope for most API versions, for example:",
                    "",
                    "MyActivity.this.getDir(\"target\", Context.MODE_PRIVATE)",
                    "or",
                    "getInstrumentation().getTargetContext().getCacheDir().getPath()"
            ));
        }
        return new AndroidClassLoadingStrategy.Injecting(target);
    }
}

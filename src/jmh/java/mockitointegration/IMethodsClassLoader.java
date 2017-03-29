package mockitointegration;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.mockitousage.IMethods;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class IMethodsClassLoader extends ClassLoader {

    private ByteBuddy byteBuddy;

    IMethodsClassLoader() {
        super(null);
        byteBuddy = new ByteBuddy();
    }

    Class<? extends IMethods> loadIMethods() {
        return byteBuddy
                .subclass(IMethods.class)
                .make()
                .load(this, ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.equals(IMethods.class.getName())) {
            try {
                InputStream inputStream = IMethods.class.getResourceAsStream(IMethods.class.getSimpleName() + ".class");
                if (inputStream != null) {
                    try {
                        byte[] buffer = new byte[1024];
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int length;
                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                        byte[] classFile = outputStream.toByteArray();
                        return defineClass(name, classFile, 0, classFile.length, null);
                    } finally {
                        inputStream.close();
                    }
                }
            } catch (IOException ignored) {
            }
        }
        return super.findClass(name);
    }
}

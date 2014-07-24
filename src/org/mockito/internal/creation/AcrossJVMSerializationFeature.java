/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.creation;

import org.mockito.Incubating;
import org.mockito.exceptions.base.MockitoSerializationIssue;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.SerializableMode;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.mockito.internal.util.StringJoiner.join;

/**
 * This is responsible for serializing a mock, it is enabled if the mock is implementing
 * {@link Serializable}.
 *
 * <p>
 *     The way it works is to enable serialization via the {@link #enableSerializationAcrossJVM(MockCreationSettings)},
 *     if the mock settings is set to be serializable it will add the {@link AcrossJVMSerializationFeature.AcrossJVMMockitoMockSerializable}
 *     interface.
 *     This interface defines a the {@link AcrossJVMSerializationFeature.AcrossJVMMockitoMockSerializable#writeReplace()}
 *     whose signature match the one that is looked by the standard Java serialization.
 * </p>
 *
 * <p>
 *     Then in the {@link MethodInterceptorFilter} of mockito, if the <code>writeReplace</code> method is called,
 *     it will use the custom implementation of this class {@link #writeReplace(Object)}. This method has a specific
 *     knowledge on how to serialize a mockito mock that is based on CGLIB.
 * </p>
 *
 * <p><strong>Only one instance per mock! See {@link MethodInterceptorFilter}</strong></p>
 *
 * TODO use a proper way to add the interface
 * TODO offer a way to disable completely this behavior, or maybe enable this behavior only with a specific setting
 * TODO check the class is mockable in the deserialization side
 *
 * @see CglibMockMaker
 * @see MethodInterceptorFilter
 * @author Brice Dutheil
 * @since 1.9.6
 */
@Incubating
public class AcrossJVMSerializationFeature implements Serializable {
    private static final long serialVersionUID = 7411152578314420778L;
    private static final String MOCKITO_PROXY_MARKER = "MockitoProxyMarker";
    private boolean instanceLocalCurrentlySerializingFlag = false;
    private final Lock mutex = new ReentrantLock();

    public boolean isWriteReplace(Method method) {
        return  method.getReturnType() == Object.class
                && method.getParameterTypes().length == 0
                && method.getName().equals("writeReplace");
    }


    /**
     * Custom implementation of the <code>writeReplace</code> method for serialization.
     *
     * Here's how it's working and why :
     * <ol>
     *     <li>
     *         <p>When first entering in this method, it's because some is serializing the mock, with some code like :
     * <pre class="code"><code class="java">
     *     objectOutputStream.writeObject(mock);
     * </code></pre>
     *         So, {@link ObjectOutputStream} will track the <code>writeReplace</code> method in the instance and
     *         execute it, which is wanted to replace the mock by another type that will encapsulate the actual mock.
     *         At this point, the code will return an
     *         {@link org.mockito.internal.creation.AcrossJVMSerializationFeature.AcrossJVMMockSerializationProxy}.</p>
     *     </li>
     *     <li>
     *         <p>Now, in the constructor
     *         {@link org.mockito.internal.creation.AcrossJVMSerializationFeature.AcrossJVMMockSerializationProxy#AcrossJVMMockSerializationProxy(Object)}
     *         the mock is being serialized in a custom way (using
     *         {@link org.mockito.internal.creation.AcrossJVMSerializationFeature.MockitoMockObjectOutputStream}) to a
     *         byte array. So basically it means the code is performing double nested serialization of the passed
     *         <code>mockitoMock</code>.</p>
     *
     *         <p>However the <code>ObjectOutputStream</code> will still detect the custom
     *         <code>writeReplace</code> and execute it.
     *         <em>(For that matter disabling replacement via {@link ObjectOutputStream#enableReplaceObject(boolean)}
     *         doesn't disable the <code>writeReplace</code> call, but just just toggle replacement in the
     *         written stream, <strong><code>writeReplace</code> is always called by
     *         <code>ObjectOutputStream</code></strong>.)</em></p>
     *
     *         <p>In order to avoid this recursion, obviously leading to a {@link StackOverflowError}, this method is using
     *         a flag that marks the mock as already being replaced, and then shouldn't replace itself again.
     *         <strong>This flag is local to this class</strong>, which means the flag of this class unfortunately needs
     *         to be protected against concurrent access, hence the reentrant lock.</p>
     *     </li>
     * </ol>
     *
     *
     * @param mockitoMock The Mockito mock to be serialized.
     * @return A wrapper ({@link AcrossJVMMockSerializationProxy}) to be serialized by the calling ObjectOutputStream.
     * @throws ObjectStreamException
     */
    public Object writeReplace(Object mockitoMock) throws ObjectStreamException {
        try {
            // reentrant lock for critical section. could it be improved ?
            mutex.lock();
            // mark started flag // per thread, not per instance
            // temporary loosy hack to avoid stackoverflow
            if(mockIsCurrentlyBeingReplaced()) {
                return mockitoMock;
            }
            mockReplacementStarted();

            return new AcrossJVMMockSerializationProxy(mockitoMock);
        } catch (IOException ioe) {
            MockUtil mockUtil = new MockUtil();
            MockName mockName = mockUtil.getMockName(mockitoMock);
            String mockedType = mockUtil.getMockSettings(mockitoMock).getTypeToMock().getCanonicalName();
            throw new MockitoSerializationIssue(join(
                    "The mock '" + mockName + "' of type '" + mockedType + "'",
                    "The Java Standard Serialization reported an '" + ioe.getClass().getSimpleName() + "' saying :",
                    "  " + ioe.getMessage()
            ), ioe);
        } finally {
            // unmark
            mockReplacementCompleted();
            mutex.unlock();
        }
    }


    private void mockReplacementCompleted() {
        instanceLocalCurrentlySerializingFlag = false;
    }


    private void mockReplacementStarted() {
        instanceLocalCurrentlySerializingFlag = true;
    }


    private boolean mockIsCurrentlyBeingReplaced() {
        return instanceLocalCurrentlySerializingFlag;
    }


    /**
     * Enable serialization serialization that will work across classloaders / and JVM.
     *
     * <p>Only enable if settings says the mock should be serializable. In this case add the
     * {@link AcrossJVMMockitoMockSerializable} to the extra interface list.</p>
     *
     * @param settings Mock creation settings.
     * @param <T> Type param to not be bothered by the generics
     */
    public <T> void enableSerializationAcrossJVM(MockCreationSettings<T> settings) {
        if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
            // havin faith that this set is modifiable
            // TODO use a proper way to add the interface
            settings.getExtraInterfaces().add(AcrossJVMMockitoMockSerializable.class);
        }
    }


    /**
     * This is the serialization proxy that will encapsulate the real mock data as a byte array.
     *
     * <p>When called in the constructor it will serialize the mock in a byte array using a
     * custom {@link org.mockito.internal.creation.AcrossJVMSerializationFeature.MockitoMockObjectOutputStream} that
     * will annotate the mock class in the stream.
     * Other information are used in this class in order to facilitate deserialization.
     * </p>
     *
     * <p>Deserialization of the mock will be performed by the {@link #readResolve()} method via
     * the custom {@link MockitoMockObjectInputStream} that will be in charge of creating the mock class.</p>
     */
    public static class AcrossJVMMockSerializationProxy implements Serializable {


        private static final long serialVersionUID = -7600267929109286514L;
        private final byte[] serializedMock;
        private final Class typeToMock;
        private final Set<Class> extraInterfaces;
        /**
         * Creates the wrapper that be used in the serialization stream.
         *
         * <p>Immediately serializes the Mockito mock using specifically crafted
         * {@link org.mockito.internal.creation.AcrossJVMSerializationFeature.MockitoMockObjectOutputStream},
         * in a byte array.</p>
         *
         * @param mockitoMock The Mockito mock to serialize.
         * @throws IOException
         */
        public AcrossJVMMockSerializationProxy(Object mockitoMock) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new MockitoMockObjectOutputStream(out);

            objectOutputStream.writeObject(mockitoMock);

            objectOutputStream.close();
            out.close();

            MockCreationSettings mockSettings = new MockUtil().getMockSettings(mockitoMock);
            this.serializedMock = out.toByteArray();
            this.typeToMock = mockSettings.getTypeToMock();
            this.extraInterfaces = mockSettings.getExtraInterfaces();
        }

        /**
         * Resolves the proxy to a new deserialized instance of the Mockito mock.
         *
         * <p>Uses the custom crafted {@link MockitoMockObjectInputStream} to deserialize the mock.</p>
         *
         * @return A deserialized instance of the Mockito mock.
         * @throws ObjectStreamException
         */
        private Object readResolve() throws ObjectStreamException {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(serializedMock);
                ObjectInputStream objectInputStream = new MockitoMockObjectInputStream(bis, typeToMock, extraInterfaces);

                Object deserializedMock = objectInputStream.readObject();

                bis.close();
                objectInputStream.close();

                return deserializedMock;
            } catch (IOException ioe) {
                throw new MockitoSerializationIssue(join(
                        "Mockito mock cannot be deserialized to a mock of '" + typeToMock.getCanonicalName() + "'. The error was :",
                        "  " + ioe.getMessage(),
                        "If you are unsure what is the reason of this exception, feel free to contact us on the mailing list."
                ), ioe);
            } catch (ClassNotFoundException cce) {
                throw new MockitoSerializationIssue(join(
                        "A class couldn't be found while deserializing a Mockito mock, you should check your classpath. The error was :",
                        "  " + cce.getMessage(),
                        "If you are still unsure what is the reason of this exception, feel free to contact us on the mailing list."
                ), cce);
            }
        }
    }


    /**
     * Special Mockito aware <code>ObjectInputStream</code> that will resolve the Mockito proxy class.
     *
     * <p>
     *     This specificaly crafted ObjectInoutStream has the most important role to resolve the Mockito generated
     *     class. It is doing so via the {@link #resolveClass(java.io.ObjectStreamClass)} which looks in the stream
     *     for a Mockito marker. If this marker is found it will try to resolve the mockito class otherwise it
     *     delegates class resolution to the default super behavior.
     *     The mirror method used for serializing the mock is
     *     {@link org.mockito.internal.creation.AcrossJVMSerializationFeature.MockitoMockObjectOutputStream#annotateClass(Class)}.
     * </p>
     *
     * <p>
     *     When this marker is found, {@link ClassImposterizer} methods are being used to create the mock class.
     *     <em>Note that behind the <code>ClassImposterizer</code> there is CGLIB and the
     *     {@link org.mockito.internal.creation.jmock.SearchingClassLoader} that will look if this enhanced class has
     *     already been created in an accessible classloader ; so basically this code trusts the ClassImposterizer
     *     code.</em>
     * </p>
     */
    public static class MockitoMockObjectInputStream extends ObjectInputStream {
        private final Class typeToMock;
        private final Set<Class> extraInterfaces;

        public MockitoMockObjectInputStream(InputStream in, Class typeToMock, Set<Class> extraInterfaces) throws IOException {
            super(in) ;
            this.typeToMock = typeToMock;
            this.extraInterfaces = extraInterfaces;
            enableResolveObject(true); // ensure resolving is enabled
        }

        /**
         * Resolve the Mockito proxy class if it is marked as such.
         *
         * <p>Uses the fields {@link #typeToMock} and {@link #extraInterfaces} to
         * create the Mockito proxy class as the <code>ObjectStreamClass</code>
         * doesn't carry useful information for this purpose.</p>
         *
         * @param desc Description of the class in the stream, not used.
         * @return The class that will be used to deserialize the instance mock.
         * @throws IOException
         * @throws ClassNotFoundException
         */
        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            if (notMarkedAsAMockitoMock(readObject())) {
                return super.resolveClass(desc);
            }

            // TODO check the class is mockable in the deserialization side
            // ClassImposterizer.INSTANCE.canImposterise(typeToMock);

            // create the Mockito mock class before it can even be deserialized
            ClassImposterizer.INSTANCE.setConstructorsAccessible(typeToMock, true);
            Class<?> proxyClass = ClassImposterizer.INSTANCE.createProxyClass(
                    typeToMock,
                    extraInterfaces.toArray(new Class[extraInterfaces.size()])
            );

            hackClassNameToMatchNewlyCreatedClass(desc, proxyClass);

            return proxyClass;

        }

        /**
         * Hack the <code>name</code> field of the given <code>ObjectStreamClass</code> with
         * the <code>newProxyClass</code>.
         *
         * The parent ObjectInputStream will check the name of the class in the stream matches the name of the one
         * that is created in this method.
         *
         * The CGLIB classes uses a hash of the classloader and/or maybe some other data that allow them to be
         * relatively unique in a JVM.
         *
         * When names differ, which happens when the mock is deserialized in another ClassLoader, a
         * <code>java.io.InvalidObjectException</code> is thrown, so this part of the code is hacking through
         * the given <code>ObjectStreamClass</code> to change the name with the newly created class.
         *
         * @param descInstance The <code>ObjectStreamClass</code> that will be hacked.
         * @param proxyClass The proxy class whose name will be applied.
         * @throws InvalidObjectException
         */
        private void hackClassNameToMatchNewlyCreatedClass(ObjectStreamClass descInstance, Class<?> proxyClass) throws ObjectStreamException {
            try {
              Field classNameField = descInstance.getClass().getDeclaredField("name");
              new FieldSetter(descInstance, classNameField).set(proxyClass.getCanonicalName());
            } catch (NoSuchFieldException nsfe) {
                // TODO use our own mockito mock serialization exception
                throw new MockitoSerializationIssue(join(
                        "Wow, the class 'ObjectStreamClass' in the JDK don't have the field 'name',",
                        "this is definitely a bug in our code as it means the JDK team changed a few internal things.",
                        "",
                        "Please report an issue with the JDK used, a code sample and a link to download the JDK would be welcome."
                ), nsfe);
            }
        }

        /**
         * Read the stream class annotation and identify it as a Mockito mock or not.
         *
         * @param marker The marker to identify.
         * @return <code>true</code> if not marked as a Mockito, <code>false</code> if the class annotation marks a Mockito mock.
         * @throws IOException
         * @throws ClassNotFoundException
         */
        private boolean notMarkedAsAMockitoMock(Object marker) throws IOException, ClassNotFoundException {
            return !MOCKITO_PROXY_MARKER.equals(marker);
        }
    }


    /**
     * Special Mockito aware <code>ObjectOutputStream</code>.
     *
     * <p>
     *     This output stream has the role of marking in the stream the Mockito class. This
     *     marking process is necessary to identify the proxy class that will need to be recreated.
     *
     *     The mirror method used for deserializing the mock is
     *     {@link MockitoMockObjectInputStream#resolveClass(ObjectStreamClass)}.
     * </p>
     *
     */
    private static class MockitoMockObjectOutputStream extends ObjectOutputStream {
        private static final String NOTHING = "";

        public MockitoMockObjectOutputStream(ByteArrayOutputStream out) throws IOException {
            super(out);
        }

        /**
         * Annotates (marks) the class if this class is a Mockito mock.
         *
         * @param cl The class to annotate.
         * @throws IOException
         */
        @Override
        protected void annotateClass(Class<?> cl) throws IOException {
            writeObject(mockitoProxyClassMarker(cl));
            // might be also useful later, for embedding classloader info ...maybe ...maybe not
        }

        /**
         * Returns the Mockito marker if this class is a Mockito mock.
         *
         * @param cl The class to mark.
         * @return The marker if this is a Mockito proxy class, otherwise returns a void marker.
         */
        private String mockitoProxyClassMarker(Class<?> cl) {
            if (AcrossJVMMockitoMockSerializable.class.isAssignableFrom(cl)) {
                return MOCKITO_PROXY_MARKER;
            } else {
                return NOTHING;
            }
        }
    }


    /**
     * Simple interface that hold a correct <code>writeReplace</code> signature that can be seen by an
     * <code>ObjectOutputStream</code>.
     *
     * It will be applied before the creation of the mock when the mock setting says it should serializable.
     *
     * @see #enableSerializationAcrossJVM(org.mockito.mock.MockCreationSettings)
     */
    public interface AcrossJVMMockitoMockSerializable {
        public Object writeReplace() throws java.io.ObjectStreamException;
    }
}

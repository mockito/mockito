/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.jar.asm.ClassReader;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.utility.OpenedClassReader;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Set;

/**
 * A {@link ClassFileTransformer} that suppresses static initializers ({@code <clinit>}) for
 * specified classes at class-load time.
 *
 * <p>When a class is first loaded (not retransformed) and its fully-qualified name appears in the
 * configured set, the transformer replaces the {@code <clinit>} method with an empty method body.
 * This causes all static fields to retain their JVM zero-value defaults ({@code null},
 * {@code 0}, {@code false}, etc.).
 *
 * <p>This transformer is registered at premain time when the system property
 * {@code mockito.suppress.clinit} is set. See {@link org.mockito.internal.PremainAttach} for
 * details.
 *
 * @since 5.+
 */
public class ClinitSuppressionTransformer implements ClassFileTransformer {

    private final Set<String> classNames;

    /**
     * Creates a new transformer.
     *
     * @param classNames fully-qualified class names whose static initializers should be suppressed
     *     (e.g. {@code "com.example.MyClass"})
     */
    public ClinitSuppressionTransformer(Set<String> classNames) {
        this.classNames = classNames;
    }

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) {
        if (classBeingRedefined != null) {
            return null;
        }
        if (className == null) {
            return null;
        }
        String dotName = className.replace('/', '.');
        if (!classNames.contains(dotName)) {
            return null;
        }
        return suppressClinit(classfileBuffer);
    }

    /**
     * Replaces the {@code <clinit>} method in the given class bytecode with an empty method body.
     * If the class has no {@code <clinit>}, the bytecode is returned unchanged.
     *
     * <p>The transformation uses ASM (shaded inside ByteBuddy) to visit all methods. When
     * {@code <clinit>} is encountered, a new empty method body containing only a {@code RETURN}
     * instruction is emitted. All other methods and class structure are preserved as-is.
     *
     * @param classfileBuffer the original class bytecode
     * @return the transformed bytecode with an empty {@code <clinit>}, or the original bytecode
     *     if no {@code <clinit>} was found
     */
    static byte[] suppressClinit(byte[] classfileBuffer) {
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(reader, 0);
        ClinitSuppressionClassVisitor classVisitor = new ClinitSuppressionClassVisitor(writer);

        reader.accept(classVisitor, 0);

        return classVisitor.found ? writer.toByteArray() : classfileBuffer;
    }

    private static class ClinitSuppressionClassVisitor extends ClassVisitor {
        boolean found;

        ClinitSuppressionClassVisitor(ClassVisitor classVisitor) {
            super(OpenedClassReader.ASM_API, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(
                int access, String name, String descriptor, String signature, String[] exceptions) {
            if ("<clinit>".equals(name)) {
                found = true;
                MethodVisitor mv =
                        super.visitMethod(access, name, descriptor, signature, exceptions);
                mv.visitCode();
                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(0, 0);
                mv.visitEnd();
                return null;
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }
}

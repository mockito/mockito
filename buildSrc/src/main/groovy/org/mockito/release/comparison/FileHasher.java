package org.mockito.release.comparison;

import org.gradle.internal.UncheckedException;
import org.mockito.release.util.InputOutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class FileHasher {

    byte[] hash(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return hash(is);
        } catch (Exception e) {
            throw new RuntimeException("Problems creating hash for file: " + file);
        } finally {
            InputOutput.closeStream(is);
        }
    }

    private static byte[] hash(InputStream instr) throws IOException {
        MessageDigest messageDigest = createMessageDigest("MD5");
        byte[] buffer = new byte[4096];
        try {
            while (true) {
                int nread = instr.read(buffer);
                if (nread < 0) {
                    break;
                }
                messageDigest.update(buffer, 0, nread);
            }
        } finally {
            instr.close();
        }
        return messageDigest.digest();
    }

    private static MessageDigest createMessageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }
    }
}

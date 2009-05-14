/*
 * Copyright 2003,2004 The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.mockito.cglib.transform;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.zip.*;

import org.apache.tools.ant.*;
import org.mockito.asm.*;
import org.mockito.cglib.core.*;

abstract public class AbstractTransformTask extends AbstractProcessTask {
    private static final int ZIP_MAGIC = 0x504B0304;

    private static final int CLASS_MAGIC = 0xCAFEBABE;

    private boolean verbose;

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * returns transformation for source class
     * 
     * @param classInfo
     *            class information 
     *            class name := classInfo[ 0 ] 
     *            super class  name := classInfo[ 1 ]
     *            interfaces := classInfo[ >1 ]
     */
    abstract protected ClassTransformer getClassTransformer(String[] classInfo);

    protected Attribute[] attributes() {
        return null;
    }

    protected void processFile(File file) throws Exception {

        if (isClassFile(file)) {

            processClassFile(file);

        } else if (isJarFile(file)) {

            processJarFile(file);

        } else {
            
            log("ignoring " + file.toURL(), Project.MSG_WARN);
            
        }
    }

    /**
     * @param file
     * @throws Exception
     * @throws FileNotFoundException
     * @throws IOException
     * @throws MalformedURLException
     */
    private void processClassFile(File file) throws Exception,
            FileNotFoundException, IOException, MalformedURLException {

        ClassReader reader = getClassReader(file);
        String name[] = ClassNameReader.getClassInfo(reader);
        ClassWriter w = new DebuggingClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassTransformer t = getClassTransformer(name);
        if (t != null) {

            if (verbose) {
                log("processing " + file.toURL());
            }
            new TransformingClassGenerator(new ClassReaderGenerator(
                    getClassReader(file), attributes(), getFlags()), t)
                    .generateClass(w);
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(w.toByteArray());
            } finally {
                fos.close();
            }

        }

    }

    protected int getFlags() {
        return 0;
    }

    private static ClassReader getClassReader(File file) throws Exception {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            ClassReader r = new ClassReader(in);
            return r;
        } finally {
            in.close();
        }

    }

    protected boolean isClassFile(File file) throws IOException {

        return checkMagic(file, CLASS_MAGIC);

    }

    protected void processJarFile(File file) throws Exception {

        if (verbose) {
            log("processing " + file.toURL());
        }
        
        File tempFile = File.createTempFile(file.getName(), null, new File(file
                .getAbsoluteFile().getParent()));
        try{
            
            ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
            try {
                FileOutputStream fout = new FileOutputStream(tempFile, false);
                try{
                 ZipOutputStream out = new ZipOutputStream(fout);
                                
                    ZipEntry entry;
                    while ((entry = zip.getNextEntry()) != null) {
                        
                        
                        byte bytes[] = getBytes(zip);
                        
                        if (!entry.isDirectory()) {
                            
                            DataInputStream din = new DataInputStream(
                                              new ByteArrayInputStream(bytes)
                                            );
                            
                            if (din.readInt() == CLASS_MAGIC) {
                                
                                bytes = process(bytes);
                                                        
                            } else {
                                if (verbose) {
                                 log("ignoring " + entry.toString());
                                }
                            }
                        }
                       
                        ZipEntry outEntry = new ZipEntry(entry.getName());
                        outEntry.setMethod(entry.getMethod());
                        outEntry.setComment(entry.getComment());
                        outEntry.setSize(bytes.length);
                        
                        
                        if(outEntry.getMethod() == ZipEntry.STORED){
                            CRC32 crc = new CRC32();
                            crc.update(bytes);
                            outEntry.setCrc( crc.getValue() );
                            outEntry.setCompressedSize(bytes.length);
                        }
                        out.putNextEntry(outEntry);
                        out.write(bytes);
                        out.closeEntry();
                        zip.closeEntry();
                        
                    }
                    out.close(); 
                }finally{
                 fout.close();    
                } 
            } finally {
                zip.close();
            }
            
            
            if(file.delete()){
                
                File newFile = new File(tempFile.getAbsolutePath());
                
                if(!newFile.renameTo(file)){
                    throw new IOException("can not rename " + tempFile + " to " + file);   
                }
                
            }else{
                throw new IOException("can not delete " + file);
            }
            
        }finally{
            
            tempFile.delete();
            
        }
        
    }

    /**
     * @param bytes
     * @return
     * @throws IOException
     * @throws Exception
     */
    private byte[] process(byte[] bytes) throws Exception {

        ClassReader reader = new ClassReader(new ByteArrayInputStream(bytes));
        String name[] = ClassNameReader.getClassInfo(reader);
        ClassWriter w = new DebuggingClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassTransformer t = getClassTransformer(name);
        if (t != null) {
            if (verbose) {
                log("processing " + name[0]);
            }
            new TransformingClassGenerator(new ClassReaderGenerator(
                    new ClassReader(new ByteArrayInputStream(bytes)),
                    attributes(), getFlags()), t).generateClass(w);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(w.toByteArray());
            return out.toByteArray();
        }
        return bytes;
    }

    /**
     * @param zip
     * @return
     * @throws IOException
     */
    private byte[] getBytes(ZipInputStream zip) throws IOException {

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        InputStream in = new BufferedInputStream(zip);
        int b;
        while ((b = in.read()) != -1) {
            bout.write(b);
        }
        return bout.toByteArray();
    }

    private boolean checkMagic(File file, long magic) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        try {
            int m = in.readInt();
            return magic == m; 
        } finally {
            in.close();
        }
    }

    protected boolean isJarFile(File file) throws IOException {
        return checkMagic(file, ZIP_MAGIC);
    }

}
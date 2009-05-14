/*
 * Copyright 2003 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mockito.cglib.transform;

import java.io.File;
import java.util.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

abstract public class AbstractProcessTask extends Task {
    private Vector filesets = new Vector();

    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }
    
    protected Collection getFiles() {
        Map fileMap = new HashMap();
        Project p = getProject();
        for (int i = 0; i < filesets.size(); i++) {
            FileSet fs = (FileSet)filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(p);
            String[] srcFiles = ds.getIncludedFiles();
            File dir = fs.getDir(p);
            for (int j = 0; j < srcFiles.length; j++) {
                 File src = new File(dir, srcFiles[j]);
                 fileMap.put(src.getAbsolutePath(), src);
            }
        }
        return fileMap.values();
    }

    
    
    public void execute() throws BuildException {
        beforeExecute();
        for (Iterator it = getFiles().iterator(); it.hasNext();) {
            try {
                processFile((File)it.next());
            } catch (Exception e) {
                 throw new BuildException(e);
            }
        }
    }

    protected void beforeExecute() throws BuildException { }
    abstract protected void processFile(File file) throws Exception;
}

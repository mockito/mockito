import shutil
import os
import os.path

def run(cmd):
  print("\nRunning command: " + cmd)
  assert os.system(cmd) == 0, "\nCommand failed: " + cmd

def go(work_dir):
    os.chdir(work_dir)

    assert os.path.exists('maven')
    
    print("Copying maven-metdata.xml files...")
    shutil.copy('maven/repository/org/mockito/mockito-all/maven-metadata.xml', 'maven/mockito-all-metadata.xml')
    shutil.copy('maven/repository/org/mockito/mockito-core/maven-metadata.xml', 'maven/mockito-core-metadata.xml')
    
    print("Checking-in maven-metdata.xml files...")

    run('svn ci -m "updated maven metadata so that versions history is properly maintained in the metadata" maven')

    in_trunk_already = os.stat('maven') == os.stat('../mockito-java/maven')
    if (not in_trunk_already):
        print("Merging the changes to maven metadata files to trunk")
        shutil.copy('maven/repository/org/mockito/mockito-all/maven-metadata.xml', '../mockito-java/maven/mockito-all-metadata.xml')
        shutil.copy('maven/repository/org/mockito/mockito-core/maven-metadata.xml', '../mockito-java/maven/mockito-core-metadata.xml')
        run('svn ci -m "updated maven metadata so that versions history is properly maintained in the metadata" ../mockito-java/maven')
    
if (__name__ == '__main__'):
    go('..')
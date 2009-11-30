import shutil
import os
import os.path

def run(cmd):
  print("\nRunning command: " + cmd)
  assert os.system(cmd) == 0, "\nCommand failed: " + cmd

def go(work_dir):
    os.chdir(work_dir)

    maven_folder = 'maven'
    assert os.path.exists(maven_folder)
    
    print("Updating maven-metdata.xml files...")
    shutil.copy('maven/repository/org/mockito/mockito-all/maven-metadata.xml', maven_folder + '/mockito-all-metadata.xml')
    shutil.copy('maven/repository/org/mockito/mockito-core/maven-metadata.xml', maven_folder + '/mockito-core-metadata.xml')
    
    print("Checking-in maven-metdata.xml files...")

    run('svn ci -m "updated maven metadata so that versions history is properly maintained in the metadata" ' + maven_folder + '/*' )

    raw_input("Make sure NOW if all you need is checked in.\nTHERE SHOULD BE NO CHANGED FILES!!!.\nProceed? (Y/N):")
    
if (__name__ == '__main__'):
    go('..')
import shutil
import os
import os.path

def run(cmd):
  print("\nRunning command: " + cmd)
  assert os.system(cmd) == 0, "\nCommand failed: " + cmd

def go(work_dir):
    os.chdir(work_dir)

    maven_folder_in_trunk = '../mockito-java/maven'
    assert os.path.exists(maven_folder_in_trunk)
    
    print("Updating maven-metdata.xml files...")
    shutil.copy('maven/repository/org/mockito/mockito-all/maven-metadata.xml', maven_folder_in_trunk + '/mockito-all-metadata.xml')
    shutil.copy('maven/repository/org/mockito/mockito-core/maven-metadata.xml', maven_folder_in_trunk + '/mockito-core-metadata.xml')
    
    print("Checking-in maven-metdata.xml files...")
    os.chdir(maven_folder_in_trunk)
    run('svn ci -m "updated maven metadata so that versions history is properly maintained in the metadata"')
    
    print("")
    print("Last step! Please perform rsync command from folder '" + work_dir + "'. This is how you do it:")
    print("Dry run:")
    print("rsync -rvn -e \"ssh -i ../rsync.mockito.key\" maven/repository/ mockito@wamblee.org:/")
    print("Run:")
    print("rsync -rv -e \"ssh -i ../rsync.mockito.key\" maven/repository/ mockito@wamblee.org:/")
    
if (__name__ == '__main__'):
    go('..')
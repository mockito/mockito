#This script is not really portable. It's just to automate some manual steps I usually do when releasing.
#It might evolve into someting more robust but for now it's ok for me.

import os
import shutil

def run(cmd):
  print("\nRunning command: " + cmd)
  if os.system(cmd) == 0: print("\nWarning, command failed: " + cmd)
  
ver = raw_input("Specify the version to try to delete, e.g. 1.9:")

branch = 'https://mockito.googlecode.com/svn/branches/' + ver
tag = 'https://mockito.googlecode.com/svn/tags/' + ver

run('svn delete -m "removed botched branch" ' + branch)
run('svn delete -m "removed botched tag" ' + tag)

shutil.rmtree("../../mockito-1.8.5", 1)
#This script is not really portable. It's just to automate some manual steps I usually do when releasing.
#It might evolve into someting more robust but for now it's ok for me.

import os

def run(cmd):
  print("\nRunning command: " + cmd)
  assert os.system(cmd) == 0, "\nCommand failed: " + cmd

ant_cmd = 'ant test.release release.maven'
raw_input("It is wise to run following command first: \n\n" + ant_cmd + "\n\nIt's because ant some times provides wrong return code.\nAny key to continue")

run('ruby replace_headers.rb')

ok = raw_input("Make sure NOW if all you need is checked in.\nTHERE SHOULD BE NO CHANGED FILES!!!.\nProceed? (Y/N):")
if ok != "Y": 
  print "Exited on user request"
  exit(1)
  
ver = raw_input("Specify the version, e.g. 1.9:")

branch = 'https://mockito.googlecode.com/svn/branches/' + ver

run('svn copy -m "branched before release" https://mockito.googlecode.com/svn/trunk ' + branch)
run('svn co ' + branch + ' ../../mockito-' + ver)

print("Switching to ../../mockito-" + ver + " folder")

branch_work_dir = '../../mockito-' + ver
os.chdir(branch_work_dir)

print("Updating version.properties to " + ver)

f = open('version.properties', 'w')
f.write('version=' + ver)
f.close()

run(ant_cmd)

run('svn add javadoc/*')
run('svn ps -R svn:mime-type text/html javadoc/*')
run('svn ps -R svn:mime-type text/css javadoc/stylesheet.css')
run('svn ci -m "released javadoc, updated version"')

import release_maven
release_maven.go('.')

print("Tagging...")
tag = 'https://mockito.googlecode.com/svn/tags/' + ver
run('svn copy -m "Tagged new release" ' + branch + ' ' + tag)
print("Removing 'latest' tag...")
latest_tag = 'https://mockito.googlecode.com/svn/tags/latest'
run('svn delete -m "Removed previous latest tag" ' + latest_tag)
print("Creating new 'latest' tag...")
run('svn copy -m "Tagged latest release" ' + branch + ' ' + latest_tag)


print("")
print("Last step! Please perform rsync command from folder '" + branch_work_dir + "'. This is how you do it:")
print("Dry run:")
print("rsync -rvn -e \"ssh -i ../rsync.mockito.key\" maven/repository/ mockito@wamblee.org:/")
print("Run:")
print("rsync -rv -e \"ssh -i ../rsync.mockito.key\" maven/repository/ mockito@wamblee.org:/")
import os

def run(cmd):
  print "\nRunning command: " + cmd
  assert os.system(cmd) == 0, "\nCommand failed: " + cmd

run('ruby replace_headers.rb')

ok = raw_input("Make sure NOW if all you need is checked in. Proceed? (Y/N):")
print ok
if ok != "Y": 
  print "Exited on user request"
  exit(1)
  
ver = raw_input("Specify the version, e.g. 1.7:")

branch = 'https://mockito.googlecode.com/svn/branches/' + ver
#run('svn copy -m "branched before release" https://mockito.googlecode.com/svn/trunk ' + branch)
#run('svn co ' + branch + ' ../../mockito-' + ver)

print "Switching to ../../mockito-" + ver + " folder"

os.chdir('../../mockito-' + ver)

print "Updating version.properties to " + ver

f = open('version.properties', 'w')
f.write('version=' + ver)
f.close()

#run('ant test.release release.javadoc')

run('svn add javadoc/*')
run('svn ps -R svn:mime-type text/html javadoc/*')
run('svn ps -R svn:mime-type text/css javadoc/stylesheet.css')
run('svn ci -m "released javadoc, updated version"')

os.chdir('releasing')
import upload_jars
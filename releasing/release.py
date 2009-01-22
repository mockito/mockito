import os

def run(cmd):
  os.system(cmd)

run('svn ruby replace_headers')

ok = input("Make sure NOW if all you need is checked in. Proceed? (Y/N):")
if ok != 'Y': 
  print "Exited on user request"
  exit(1)

ver = input("Specify the version, e.g. 1.7:")

branch = 'https://mockito.googlecode.com/svn/branches/' + ver
run('svn copy -m "branched before release" https://mockito.googlecode.com/svn/trunk ' + branch)
run('svn co ' + branch + ' ../../mockito-' + ver) 

run('svn st')

run('svn ps -R svn:mime-type text/html ../javadoc/*')
run('svn ps -R svn:mime-type text/css ../javadoc/stylesheet.css')

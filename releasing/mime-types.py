import os
os.system('svn ps -R svn:mime-type text/html ../javadoc/*')
os.system('svn ps -R svn:mime-type text/css ../javadoc/stylesheet.css')

#TODO - autoproperies in svn?
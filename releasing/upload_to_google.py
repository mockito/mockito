print("Uploading binaries to the googlecode")
ver = raw_input("Specify the version, e.g. 1.9:")

import google_upload
import sys

sys.argv.append('--project=mockito')
sys.argv.append('--user=szczepiq')
base_args = sys.argv[:]

sys.argv.append('--summary=All jars, source and javadocs')
sys.argv.append('../target/mockito-' + ver + '.zip')

google_upload.main()

sys.argv = base_args
sys.argv.append("--summary=Single jar, includes source")
sys.argv.append('../target/mockito-all-' + ver + '.jar')

google_upload.main()

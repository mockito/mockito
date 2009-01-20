import google_upload
import sys

version = 1.7

sys.argv.append('--project=mockito')
sys.argv.append('--user=szczepiq')
base_args = sys.argv

sys.argv.append('--summary="All jars, source and javadocs"')
sys.argv.append('../target/mockito-1.7.zip')

google_upload.main()

sys.argv = base_args
sys.argv.append('--summary="Single jar, includes source"')
sys.argv.append('../target/mockito-all-1.7.jar')

google_upload.main()
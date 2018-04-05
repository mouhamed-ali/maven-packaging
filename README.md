# maven-packaging
A little project which uses the maven plugins to create distributions files.
 
 ## maven-packaging-example Module
 This project creates a runnable distribution file including all dependencies, configuration files, and scripts to run the example application.
 Configuration files will be in the outside of the runnable jar file so we can change this files without creating a new distribution file.
 
  ## maven-packaging-assembly Module
  The same as the maven-packaging-example Module but the distribution file will be a tar.gz file this time. this project uses maven assembly plugin to 
  create the distribution file.
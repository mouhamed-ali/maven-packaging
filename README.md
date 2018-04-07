# maven-packaging
A little project which uses the maven plugins to create distributions files.
 
 ## maven-packaging-example Module
 This project creates a runnable distribution file including all dependencies, configuration files, and scripts to run the example application.
 Configuration files will be in the outside of the runnable jar file so we can change this files without creating a new distribution file.
   ####you can build this project by running the following command :
   mvn validate && mvn clean install
   ####to run the program use this commands :
   cd target
   cd project
   cd project-version
   java -jar project-build-jar
 
  ## maven-packaging-assembly Module
  The same as the maven-packaging-example Module but the distribution file will be a tar.gz file this time. this project uses maven assembly plugin to 
  create the distribution file.
   ####you can build the assembly by running the following command :
   mvn clean install
   ####to run the program use this commands :
   cd target
   tar -xvzf project.tar.gz
   cd project
   cd project-version
   cd bin
   java -jar project-build-jar
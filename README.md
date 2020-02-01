# maven-packaging
A little project which uses the maven plugins to create distributions files.
 
 ## maven-packaging-example Module
 This project creates a runnable distribution file including all dependencies, configuration files, and scripts to run the example application.
 Configuration files will be in the outside of the runnable jar file so we can change this files without creating a new distribution file.
   #### you can build this project by running the following command :
   mvn validate && mvn clean install
   #### to run the program use this commands :
   cd target  
   cd project  
   cd project-version  
   java -jar project-build-jar  
 
  ## maven-packaging-assembly Module
  The same as the maven-packaging-example Module but the distribution file will be a tar.gz file this time. this project uses maven assembly plugin to 
  create the distribution file.
   #### you can build the assembly by running the following command :
   mvn clean install
   #### to run the program use this commands :
   cd target  
   tar -xvzf project.tar.gz  
   cd project  
   cd project-version  
   cd bin  
   java -jar project-build-jar

   ## spring-profile-packaging-example Module
   1. mvn clean install
   2. goto the target directory
   3. move the common and config-files directories to an another place
   4. run the app 'java -jar spring-profile-packaging-example.jar'
   5. introduce the new properties locations (for the common and the config-files directories)
   6. Test this endpoints ; http://localhost:8080/ ; http://localhost:8080/description

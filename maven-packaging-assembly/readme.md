# Maven assembly

In this example, we will show you how to use the maven assembly plugin to create a distributable archive for a spring boot application.

## Overview

The Assembly Plugin for Maven is primarily intended to allow users to aggregate the project output along with its dependencies, modules, site documentation, and other files into a single distributable archive.

This is a brief description from the assembly site :

- https://maven.apache.org/plugins/maven-assembly-plugin/

So in this example, we gonna use this plugin to make a delivery which have this structure :

```
assembly-packaging-1.0-SNAPSHOT-TIMESTAMP.tar.gz
|
└─── assembly-packaging-1.0-SNAPSHOT-TIMESTAMP
|    |
|    └─── assembly-packaging
|         |
|         └─── 1.0-SNAPSHOT
|              |
|              └─── bin
|              |    └─── assembly-packaging.jar
|              |    └─── start.sh
|              |    └─── stop.sh
|              |
|              └─── conf
|              |    └─── application.properties
|              |    └─── app-parameters.properties
|              |    └─── logback-spring.xml
|              |
|              └─── lib
|              |    └─── *.jar files
|              |
|              └─── sql
|                   └─── create-db.sql
|                   └─── delete-data.sql
|                   └─── insert-data.sql
|                   └─── update-data.sql
| 
| 
```

The delivery file to send to our client is assembly-packaging-1.0-SNAPSHOT-TIMESTAMP.tar.gz which assembly-packaging is the `${delivery.file.name}`
1.0-SNAPSHOT is the `${project.version}` and TIMESTAMP is the `${timestamp}` of the build of the archive. You can find all these parameters in the pom.xml file.

As you can see in the structure :
 
1. the bin directory contains our application jar and some scripts to run and stop it,
2. the conf directory contains our app configuration files,
3. the lib directory contains the jar dependencies of our project,
4. the sql directory contains the sql scripts to create and populate our database.

### bin directory

### conf directory

### lib directory

### sql directory


### Running the app

```shell script
$ cd target && \
   tar -xvzf ${PROJECT-NAME}-${PROJECT-VERSION}-${TIMESTAMP}.tar.gz && \
   cd ${PROJECT-NAME}-${PROJECT-VERSION}-${TIMESTAMP} && \
   cd ${PROJECT-NAME} && \
   cd ${PROJECT-VERSION} && \  
   cd bin && \
   java -jar ${PROJECT-NAME}.jar
```

You can simply run the unit tests using this command (in the current directory):

```shell script
$ mvn test
```
Or using you IDE. Here is my intellij report :

![junit-sql](https://user-images.githubusercontent.com/16627692/72684705-3e928b80-3ae3-11ea-9332-4899a41bf270.png)

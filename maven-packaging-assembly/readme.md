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

This directory contains our application and two scripts to run and stop the app. We have used the `ApplicationPidFileWriter` spring boot listener
which will create a file contains the pid of our app. This file will be located in the bin directory which gonna facilitate the start and the shutdown
of the app.

### conf directory

This directory will contains all the configurations files of our app. These files are in the outside of the runnable jar file so we can change 
these files without creating a new distribution file.

### lib directory

This contains all the dependencies of application.

`PN` : as the oracle jdbc driver does not exist in the maven remote repository, we have created a local one to store it.
You can find this repo in the pom.xml file and the oracle jar file in the dependencies project which means it is safe for
anyone we use this project.

After pulling it, He should run `mvn validate` to create the local repo (using the jar oracle) and then we can package and run our app.

### assembly description

To know more about how to structure your delivery :

- check the pom.xml to know how to declare variables and how to generate the timestamp
- all the structure definition of our delivery file is in the `src/assembly/assembly-descriptor.xml`

### sql directory

This directory contains an example of sql scripts to update the database or create it. Well the sql scripts to upgrade the database.

### Running the app

This is 

Create the delivery file :

```shell script
$ mvn validate && mvn clean package
```

Run the app :

```shell script
$ cd target && \
    tar -xvzf ${PROJECT-NAME}-${PROJECT-VERSION}-${TIMESTAMP}.tar.gz && \
    cd ${PROJECT-NAME}-${PROJECT-VERSION}-${TIMESTAMP} && \
    cd ${PROJECT-NAME} && \
    cd ${PROJECT-VERSION} && \  
    cd bin && \
    chmod -R +x . && \
    sh start.sh
```

or you can just run the `run.dev.sh` script from the resources file. This script will build and run the project. If you modify the version or the app name you have to make these changes in the script also.

To stop the app, you can click on ctrl+C form the current console or use the `stop.sh` script. You will find it in the bin directory from the generated delivery archive.

### Running Tests

After running the app you should have a line like this (if everything is OK):

```log
15:19:23.688 [main] INFO  o.s.j.e.a.AnnotationMBeanExporter - Registering beans for JMX exposure on startup
15:19:23.697 [main] INFO  o.a.coyote.http11.Http11NioProtocol - Initializing ProtocolHandler ["http-nio-8090"]
15:19:23.705 [main] INFO  o.a.coyote.http11.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8090"]
15:19:23.710 [main] INFO  o.a.tomcat.util.net.NioSelectorPool - Using a shared selector for servlet write/read
15:19:23.728 [main] INFO  o.s.b.c.e.t.TomcatEmbeddedServletContainer - Tomcat started on port(s): 8090 (http)
15:19:23.732 [main] INFO  o.m.tutorial.examples.Application - Started Application in 1.569 seconds (JVM running for 1.779)
```

And the delivery content will be :

![delivery-content](https://user-images.githubusercontent.com/16627692/73457878-a4e59c80-4374-11ea-97f8-34cc41b92684.png)

Let's test our controller.

* null property

```shell script
$ curl localhost:8090/app/properties
```

```log
Oops, you can't use this service until you provide a property name
```

* exist property

```shell script
$ curl localhost:8090/app/properties?property=log.directory
```

```log
log.directory -> /home/user/dir/logs
```

```shell script
$ curl localhost:8090/app/properties?property=server.port
```

```log
server.port -> 8090
```

* not found property

```shell script
$ curl localhost:8090/app/properties?property=notFound.property
```

```log
Cannot found this property
```

And now we gonna stop the app by :

- going to the bin directory. This dir was generated when we decompressed our delivery file (using the run.dev.sh script)
in my case this dir is located here `target/assembly-packaging-1.0-SNAPSHOT-01302020.151918/assembly-packaging/1.0-SNAPSHOT/bin`

- executing the `stop.sh` script

From the parent dir, go to the conf directory and :

1. modify the `logback-spring.xml` by making the root log level to WARN `<root level="WARN">`
2. add a new property to the app-parameters.properties file. I added custom.prop `custom.prop=Hello-World!!!`
3. go to the application.properties and modify the port to 8070 `server.port=8090`

Now, return to the bin dir and run the start.sh script.

As we can see, this time we haven't much logs :

```log
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::       (v1.5.10.RELEASE)

WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.springframework.cglib.core.ReflectUtils$1 (file:/home/salto/tutorials/java/maven-packaging/maven-packaging-assembly/target/assembly-packaging-1.0-SNAPSHOT-01302020.151918/assembly-packaging/1.0-SNAPSHOT/lib/spring-core-4.3.14.RELEASE.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)
WARNING: Please consider reporting this to the maintainers of org.springframework.cglib.core.ReflectUtils$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
```

* exist property

```shell script
$ curl localhost:8070/app/properties?property=custom.prop
```

```log
custom.prop -> Hello-World!!!
```

```shell script
$ curl localhost:8070/app/properties?property=server.port
```

```log
server.port -> 8070
```

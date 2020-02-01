# Maven assembly

In this example, we will show you how to use a set of maven plugins to customize you delivery for a spring boot application.

## Overview

In this example we will use these maven plugins :

1. maven-install-plugin : as the oracle jdbc plugin is not provided in the maven remote repository we will put it in a directory in our project. This directory is `dependencies`.
On executing the `mvn validate` command, this plugin will be triggered to install the ojdbc jar in your maven local repository.

2. maven-dependency-plugin : this plugin is used to copy all the dependencies of our project to the lib directory. First of all, he will create the `${delivery.dir}/lib/` directory
which `${delivery.dir}` is a maven property (check the pom.xml to get its value) and the copy the dependencies to it.

* delivery.dir : this property value is `${project.build.directory}/${artifactId}-${buildTimeStamp}/${version}`.
    * project.build.directory : is the build directory which is target
    * artifactId : is ou artifact which is maven-packaging-example
    * buildTimeStamp : is the timestamp of the build of the project
    * version : is the project version which is 1.0-SNAPSHOT
    
3. maven-jar-plugin : this plugin is used to create the jar of our application and configure it. It will configure its class path (which gonna contain our configuration file), the main class
to run our application and the dependencies to be used (lib directory).

4. buildnumber-maven-plugin : this plugin is used to generate the timestamp which is used to name the our delivery directory.    

So in this example, we gonna use this plugin to make a delivery directory which have this structure :

```
maven-packaging-example-TIMESTAMP
|
└─── 1.0-SNAPSHOT
|    |
|    └─── bin
|    |    └─── maven-packaging-example-VERSION.jar
|    |    └─── start-stop-daemon.sh
|    |
|    └─── conf
|    |    └─── application.properties
|    |    └─── app-parameters.properties
|    |    └─── logback-spring.xml
|    |
|    └─── lib
|    |    └─── *.jar files
|    |
|    └─── sql
|         └─── select.sql
| 
| 
```

The delivery is a directory so you can compress it and send it to you client. 

If you check the pom.xml file you gonna find that this property `delivery.dir` has `${project.build.directory}/${artifactId}-${buildTimeStamp}/${version}` as value. This is the content and the tree of our delivery. Here's some explanations :
* project.build.directory : is the build directory which is target
* artifactId : is the artifact of our project which is maven-packaging-example
* buildTimeStamp : is the timestamp of the build of our delivery
* version : is the version of the app which is 1.0-SNAPSHOT

As you can see in the structure :
 
1. the bin directory contains our application jars and a script to start,stop,restart ...
2. the conf directory contains our app configuration files,
3. the lib directory contains the jars dependencies of our project,
4. the sql directory contains the sql scripts to create and populate our database.

### bin directory

This directory contains the jar application and a script named `start-stop-daemon.sh`. We have used the `ApplicationPidFileWriter` spring boot listener
which will create a file contains the pid of our app. This file will be located in the bin directory (its name is `maven-packaging.pid`) and will be used by our script to manage the app.

The script offers these features :

First of all you have to package the app `mvn validate && mvn clean package` and from the generated bin directory make the script executable `chmod +x start-stop-daemon.sh`.

#### Start the app

Just run this command from the bin directory :

```shell script
$ ./start-stop-daemon.sh start
```

#### Get status

```shell script
$ ./start-stop-daemon.sh status
```

#### Restart the app

```shell script
$ ./start-stop-daemon.sh restart
```

#### Stop it

```shell script
$ ./start-stop-daemon.sh stop
```

### conf directory

This directory will contains all the configurations files of our app. These files are in the outside of the runnable jar file so we can change 
these files without creating a new distribution file.

### lib directory

This contains all the dependencies of application.

`PN` : as the oracle jdbc driver does not exist in the maven remote repository, we have used  maven-install-plugin to install it on ou local maven repository.
You can find this config in the pom.xml file and the oracle jar file in the dependencies directory which means it is safe for
anyone we use this project.

After pulling the project, you should run `mvn validate` to install the ojdbc jar.

### sql directory

This directory contains an example of sql scripts to upgrade your database. As this is a simple example, we have created a simple endpoint to show the content of the `select.sql` file
which will be located in the classpath. The class that will load this file is `TestDataBaseConnectivity`, if it does not exist in the classpath we will look for it in the same package as
the test class. Check the tests section for more details.


### Running the app

This is a simple spring boot application which exposes two endpoints, the first to expose all properties of the app and the second will expose the select.sql content.

Create the delivery file :

```shell script
$ mvn validate && mvn clean package
```

And the delivery content should be :

![delivery-content](https://user-images.githubusercontent.com/16627692/73541609-2dc30d80-4433-11ea-9ab1-1bf0ae29a8c4.png)

A directory that has `maven-packaging-example-TIMESTAMP` as name will be created in the target file. Go to :

- `maven-packaging-example-${TIMESTAMP}` > `${VERSION}` > `bin`  

and make this script executable :

```shell script
$ chmod +x start-stop-daemon.sh
```

Now you can use it to start/stop the app. To get the usage run this :

```shell script
$ ./start-stop-daemon.sh
```

```log
Usage: ./start-stop-daemon.sh {start|stop|restart|status}
```

To get the classpath of the app have a look at the MANIFEST.MF file inside the generated jar.

### Running Tests

Let's make test on our controller.

* null property

```shell script
$ curl localhost:8090/app/properties
```

```log
Oops, you can't use this service until you provide a property name
```

#### Properties

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

#### Sql script

```shell script
$ curl localhost:8090/sql/test
```

```sql
SELECT
*
FROM
USERS;
```

#### logs

check the `nohup.out` file to get logs. Notice that the log level is INFO.

#### Modification of the config files

Always in the generated bin directory, go to the super dir (cd ..), and then to the conf directory and :

1. modify the `logback-spring.xml` by making the root log level to WARN `<root level="WARN">`
2. add a new property to the app-parameters.properties file. I added this `custom.prop=Hello-World!!!`
3. go to the application.properties and modify the port to 8070 `server.port=8070`

Go to the super directory (cd ..) and go inside the sql dir. Replace the select.sql content with this `DELETE * FROM CUSTOMERS;`.

Now, return to the bin dir and restart the app.

##### Properties

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

##### Sql script

```shell script
$ curl localhost:8070/sql/test
```

```sql
DELETE * FROM CUSTOMERS;
```

If you check the source code, you will notice that if the app can't find the select.sql script in the classpath it's gonna load it from the 
`org.maven.tutorial.examples` package. Let's delete the select.sql from the sql directory.

From sql directory run this :

```shell script
$ rm select.sql
```

And rerun the last curl :

```shell script
$ curl localhost:8070/sql/test
```

```sql
SELECT
1
FROM
DUAL;
```

##### logs

check the `nohup.out` file to get logs. Notice that the log level is WARNING this time.
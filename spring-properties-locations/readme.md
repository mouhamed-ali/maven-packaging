# Description

in this example we will show you how you can override your spring boot application without changing anything in your code.

## Overview

This is a simple spring web application that exposes the properties of the app as a rest service. All what you have to do is to call :

```shell script
$ curl localhost:8090/app/properties?property=$[property-name}
```

by replacing the ${property-name} with the needed property name. The list of properties is mentioned in the `application.yml` file.

```yaml
server:
  port: 8090

logging:
  file:
    name: app.log
    path: /tmp
    max-size: 10MB
    max-history: 7
    clean-history-on-start: true
  pattern:
    dateformat: yyyy-MM-dd HH:mm:ss.SSS

spring:
    application:
      name: properties-locations

debug: true

```

You can find all the list of the spring properties here :

- https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html#core-properties

To override the config of this app, we have to know how spring will load our application.yml and in which location he will search.

According to `ConfigFileApplicationListener`:
```java
private static final String DEFAULT_SEARCH_LOCATIONS =  "classpath:/,classpath:/config/,file:./,file:./config/";
```

And as mentioned is the documentation, SpringApplication loads properties from application.properties files in the following locations 
and adds them to the Spring Environment:
1. A config subdirectory of the current directory
2. The current directory
3. A classpath /config package
4. The classpath root

Check this link for details :

- https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-command-line-args


## Installing the app

Package the app using this command :

```shell script
$ mvn clean package
```

Goto the target directory and check for the generated jar. You should have a jar named `spring-properties-locations-1.0-SNAPSHOT.jar` :

```shell script
$ cd target && ls *.jar
```

```log
spring-properties-locations-1.0-SNAPSHOT.jar
```

run the application using this command :

```shell script
$  java -jar spring-properties-locations-1.0-SNAPSHOT.jar
```

![spring-log-before](https://user-images.githubusercontent.com/16627692/73570274-5fa49600-446c-11ea-8043-01b1a1aa926e.png)

As we can see, the log level is debug. Let's get some properties :

```shell script
$ curl localhost:8090/app/properties?property=spring.application.name
```

```log
spring.application.name -> properties-locations
```

```shell script
$ curl localhost:8090/app/properties?property=logging.file.name
```

```log
logging.file.name -> app.log
```

Let's stop the app and modify some properties. We gonna create a new file named `application.yml` in the same directory as the jar file. Its content will be :

```yaml
server:
  port: 8070

logging:
  file:
    name: application.log
    path: /tmp
    max-size: 10MB
    max-history: 7
    clean-history-on-start: true
  pattern:
    dateformat: yyyy-MM-dd HH:mm:ss.SSS

spring:
    application:
      name: properties-locations-2

debug: true
```

We have changed :

1. the spring application name to `properties-locations-2`
2. the logging file name to `application.log`
3. the server port to `8070`

Re-run and re-test the app :

```shell script
$ curl localhost:8070/app/properties?property=spring.application.name
```

```log
spring.application.name -> properties-locations-2
```

```shell script
$ curl localhost:8070/app/properties?property=logging.file.name
```

```log
logging.file.name -> application.log
```


The server port has been changed to 8070 and the the modified properties also.

Now we gonna stop the app, leave the last application.yml file and in the same directory (target) we will create a new directory named `config`. In this last directory, we will create a new file named
`application.yml` which have this content :

```yaml
server:
  port: 8090

logging:
  file:
    name: spring.boot.log
    path: /tmp
    max-size: 10MB
    max-history: 7
    clean-history-on-start: true
  pattern:
    dateformat: yyyy-MM-dd HH:mm:ss.SSS

spring:
    application:
      name: properties-locations-3

debug: false
```

We have changed :

1. the spring application name to `properties-locations-3`
2. the logging file name to `spring.boot.log`
3. the debug to `false`

From the target directory, run this command :

```shell script
$ java -jar spring-properties-locations-1.0-SNAPSHOT.jar --server.port=9000
```

Re-run the app now. You'll notice that the log level has changed to INFO.

![spring-log-after](https://user-images.githubusercontent.com/16627692/73572646-9af59380-4471-11ea-8340-fc7a4121865f.png)

And the port number has be overridden to 9000 now. You can always override properties using the command line.

```shell script
$ curl localhost:9000/app/properties?property=spring.application.name
```

```log
spring.application.name -> properties-locations-3
```

```shell script
$ curl localhost:9000/app/properties?property=logging.file.name
```

```log
logging.file.name -> spring.boot.log
```
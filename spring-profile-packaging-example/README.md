# Description

in this example we will show you how to externalize a spring boot properties files

## Overview

There is so many ways to configure spring boot to load the config files (the famous application.[properties/yml] files) of the app from an external resource. The is in :

- spring.config.name : this is the famous application prefix that we use to name our config files. If you do not like application.properties as the configuration file name, 
you can switch to another file name by specifying a spring.config.name environment property.

- spring.config.location : this is the location of the config files. It is a comma-separated list of directory locations or file paths.

You can pass these properties as a java -jar arguments or specify them as environment parameters. Please have a look at the documentation for more details :

- https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html

In this example we will ask the user to provide these parameters and then we will pass them as properties of the `SpringApplicationBuilder` class. Check the   `Application.java` file.

### Structure

Our project structure is as follow :

![delivery-content](https://user-images.githubusercontent.com/16627692/73554333-d1b8b300-444b-11ea-9794-50a628671d6a.png)

When building the app, we will launch the maven resources configuration (check pom.xml file). In this config we will :

1. parse the resources and leave only the `application-version.yml` file and filtering it. As a result, the generated jar will have only this file in its resources.
check `<!-- step 1 -->` in the pom.xml. What means filtering the file ?

if you open the `application-version.yml` you will find some declarations like this one `version: @project.version@`. When we choose to filter, maven will parse the yml file and search for properties
`@timestamp@` for example and then he will replace them with theirs values declared in the pom.xml `<timestamp>timestamp-value</timestamp>`

2. we will externalize the common properties by placing them in the target directory / common

3. externalize the application.yml and application-${springActiveProfile}.yml by placing them in the target directory / config-files directory. As you see we've used filters this time again. Maven will
externalize application-dev.yml if you are declaring this property in the pom.xml `<springActiveProfile>dev</springActiveProfile>`.

### Endpoints

Have a look at the `ShowProperties.java` file, you will notice that :

- we are exposing a `/platform` endpoint, this will load and display the `application.platform` property. This property is defined in the application-${springActiveProfile}.yml file.

- we are exposing a `/description` endpoint, this will load and display the `description` property. This property is defined in the description.properties file.

As we are using actuator (check the dependencies of the project), the info endpoint will be configured and exposed automatically.

- host:[server.port]/info

We have customized the response of this endpoint in `application-version.yml` file. As this file is not externalized, the user can't modify it and he can't change the build time of the delivery file.
The exposed info are :

| Endpoint  | Description  | 
|---|---|
| info.app.name  |  application name |
| info.app.description  |  description |
| info.app.version | the version of the app  |
| info.app.build-time | the build time of the app  |

build-time is not an actuator property. it is a custom property that we've added.

## Installing the app

Package the app using this command :

```shell script
$ mvn clean package
```

Goto the target directory and move the common and config-files directories to an another place :

```shell script
$ if [ -d "/tmp/commons" ]; then rm -Rf /tmp/commons; fi && \
  mkdir /tmp/commons && cp -r common/* /tmp/commons/ 
```

I'm if the directory exists or not before deleting it.

```shell script
$ if [ -d "/tmp/configs" ]; then rm -Rf /tmp/configs; fi && \
  rm -rf /tmp/configs/* && mkdir /tmp/configs && cp -r config-files/* /tmp/configs/
```

As you can see, I placed them in the tmp directory and i changes its names. Now we gonna run the application :

```shell script
$ java -jar spring-profile-packaging-example.jar
```

The app will ask you for the config directories so you have to introduce them :

![spring](https://user-images.githubusercontent.com/16627692/73558107-d0d74f80-4452-11ea-906e-73da948c825c.png)

### Running Tests

As we are using dev as a profile, we gonna test the platform endpoint :

```shell script
$ curl localhost:8080/platform
```

```log
development
```

Get the description.properties content :
 
```shell script
$ curl localhost:8080/description
```
 
```log
Hello users, This app will externalize the config files of our spring boot application
```

And the actuator endpoint :

```shell script
$ curl localhost:8080/info
```

```json
{"app":{"name":"externalization","description":"externalize the applicaiton properties of a spring project based its profiles","version":"1.0-SNAPSHOT","build-time":"01/31/2020 16:51:40:599"}}
```

And now we gonna make some changes to the config files.

Stop the app, and modify the content of the application-dev.yml file. In my case it will be :

```shell script
$ nano /tmp/configs/application-dev.yml
```

```yml
application:
  platform: develop   
```

and the content of description.properties :

```shell script
$ nano /tmp/commons/description.properties
```

```properties
description=Hello customers, it is hot today 
```

Rerun the app and make these tests :


```shell script
$ curl localhost:8080/platform
```

```log
develop
```
 
```shell script
$ curl localhost:8080/description
```
 
```log
Hello customers, it is hot today
```



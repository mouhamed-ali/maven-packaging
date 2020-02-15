# Maven Packaging

This repository provides some examples to show how you can create a distribution file for your spring boot application using maven.

Examples uses different maven plugins (each example uses one or more than one plugin) to create the delivery file:
* maven assembly
* maven-jar-plugin
* spring-boot-maven-plugin
* ...

This is the list of examples :

```
maven-packaging
│
└─── maven-packaging-assembly
└─── maven-packaging-example
└─── spring-profile-packaging-example
└─── spring-properties-locations
```

### Prerequisites

To create your development environment, you will need to :

- install java 8
- install maven
- install git to clone the project
- you favorite IDE (i'm using intellij)

### Installing

To clone the project, run this command :

```
    $ git clone https://github.com/amdouni-mohamed-ali/maven-packaging.git
    $ cd maven-packaging
```

And run these commands in the order to package the project :

```
    $ mvn clean validate
    $ mvn clean package
```

```log
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] maven-packaging .................................... SUCCESS [  0.213 s]
[INFO] maven-packaging-example ............................ SUCCESS [  7.147 s]
[INFO] maven-packaging-assembly ........................... SUCCESS [ 40.681 s]
[INFO] spring-profile-packaging-example ................... SUCCESS [  3.497 s]
[INFO] spring-properties-locations ........................ SUCCESS [  0.439 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 52.144 s
[INFO] Finished at: 2020-02-15T22:05:26+01:00
[INFO] Final Memory: 54M/458M
[INFO] ------------------------------------------------------------------------

Process finished with exit code 0
```

## Built With

* [Java](https://openjdk.java.net/) - java version "1.8.0_101"
* [Maven](https://maven.apache.org/) - Dependency Management (version 3.6.0)
* [Intellij](https://www.jetbrains.com/) - IDE (version 2018.3.3)

## Authors

* **Mohamed Ali AMDOUNI**
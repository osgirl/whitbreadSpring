# Whitbread Premier Inn Code Test Jan 2019

Author: James Lau
jameslauwf@gmail.com

## Contents
<!-- MarkdownTOC -->

- Requirements
- Git Repository
- Design
  - Spring Cloud library and reducing boilerplate code
    - Lombok
    - Netflix Feign
- Steps
  - Build
  - Running the Application
    - Shell startup scripts
    - Docker
- Issues
  - Log4j library conflicts preventing Debug level statements

<!-- /MarkdownTOC -->


## Requirements 

You are tasked with creating a microservice which allows you to search for a place by name and return the recommended or popular venues near that location, using the Foursquare API and returning the results in JSON format. 


## Git Repository

[GitHub jlau78/whitbreadSpring Repository](https://github.com/jlau78/whitbreadSpring)


## Design

The Demo application makes calls to the Foursquare API for Venue query and recommendations information. 

To access the the Demo application go to the following URLs:

[Swagger UI for Demo Application localhost:8085](http://localhost:8085/swagger-ui.html)

[Sample London cafe Venue query /venue/search](http://localhost:8085/venue/search?venue=london&query=cafe)

**Notes**
The server.port is set to 8085 to avoid port conflicts when starting Tomcat. This is done in case developers have an application already running on the default port 8080.


### Spring Cloud library and reducing boilerplate code

#### Lombok

The [Lombok libraries](https://projectlombok.org/) are used to reduce the need to creating boilerplate getter and setter code. I expect you are using the Lombok libraries but if not here is a short summary to get started.

The Lombok libraries will generate the required getters and setters at compile time. However to allow your IDE to link to them you will need to enable Lombok support in your IDE. For the Eclipse IDE which I use, refer to [Setup Lombok for Eclipse](https://projectlombok.org/setup/eclipse) for instructions on how to install the Lombok feature.

#### Netflix Feign

The Netflix Feign libraries are used for making calls to the Foursquare Rest APIs. Such calls are not boilerplate code, but these libraries reduces any 'plumbing' code required to manage such remote calls. 

Other Cloud features are not implemented in this demo,but the use of the Feign libraries works nicely with features like the Hystrix circuit breaker and Ribbon load balancing libraries among others. Hence the choice to use it for this feature.


## Steps

### Build

The application is built using Maven. I have included the built Snapshot Jar in the target directory so you can run the application without having to run a build. 

**My Code build specs**

Maven home: C:\apps\apache-maven-3.5.2
Java version: 1.8.0_131, vendor: Oracle Corporation


### Running the Application

#### Shell startup scripts

You can run the application directly with the provide scripts for convenience:

Linux/Cygwin:   startBootApp.sh
Windows:        startBootApp.win.sh

#### Docker

I have also provided a script to run the application in a Docker container. On my Windows PC I am not able to run Docker due to HyperVisor being enabled for my VirtualBox VMs. So only a shell script has been provided to be run in Linux: startdockerimages.sh

This script will build/rebuild the Docker image and run the image.


## Issues

### Log4j library conflicts preventing Debug level statements

The spring-boot-starter dependency has a sl4j class causing class binding issues with the apache log4j loaded by another dependency. This prevents debug level logging.

This could be overcome in the Eclipse IDE by manually removing the offending logback-classic and logback-core libraries. This allows verbose wire logging of the Feign calls to the Foursquare API, provided by the configuration: feign.logLevel=Full

It is not possible to fix this in the time scale.


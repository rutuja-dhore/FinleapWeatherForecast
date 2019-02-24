## Weather Forecast
Spring boot project to calculate

Average temperature (in Celsius) of the next 3 days from today's date for Day time (06:00 - 18:00) and Night time (18:00 - 06:00).

Average of pressure for the next 3 days from today's date.

#### Technology used
- Spring boot : Web application 
- Maven : Building project
- Java 8 : programming language 
- Swagger : RESTful Documentation
- Retrofit : Java Rest Client

#### Web Application configuration 
- Web Application is running on port `8080`
- You can change the configuration in `application.properties`
 
#### Steps to run the project

> Prerequisite
- Maven 
- Java 8 or higher 

> Start the application

Through following command:

    mvn clean install spring-boot:run 
    
Maven will install all the dependencies and followed by run the spring boot application

    
> To view the RESTful APIs in the application

    http://localhost:8080/swagger-ui.html
    
    
#### Architectural points and Terminologies used in the project

> Trying out and documentation for RESTful APIs

Swagger is already integrated and can be used for using API instead of curl. 

    http://localhost:8080/swagger-ui.html


> Modularise

For the time being I have modularise the project on package level.
Later we can also modularise in maven modules.    
    
> Event driven architecture

To communicate when transaction and statistics, spring-framework's application event publisher is used.

> Developer should take ownership of code, hence testing is IMPORTANT!

There are two type of tests written within the project:
    
    1. Integration Test (REST Layer Classes)
    2. Unit Testing (Service Layer Classes)
    
    To check the result of the test, please throw following command in the terminal:
    
    > mvn clean install

> Comments in code 

Entire code styling is influenced by Clean Code principle - Robert Martin
Which says
'Truth can only be found in one place: the code’.
So you may not found any comments anywhere in the project.
Keeping in mind that git can be used to versioning of file and method, class names should be kept as self explanatory.

However, if you need comments on each file. I can do that too.

> Design principles used in Project :

- SOLID (single responsibility, open-closed, Liskov subsitution, interface segragation, dependency inversion) principle
- Composition over inheritance
- DRY(Don’t repeat yourself)
- KISS(Keep it simple, stupid)
- Event based programming
- and some experience principle ;)   

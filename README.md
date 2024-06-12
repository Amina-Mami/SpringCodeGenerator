# CodeGen 
Spring Boot Code Generator.

About:
-----------------------------
The Spring Boot Code Generator is a powerful tool designed to simplify and accelerate the development process for web applications using React.js and Spring frameworks. This tool empowers users to effortlessly create fully-functional projects by defining entities, enums, and relationships, thus significantly reducing development time and effort.

Assumptions:
-----------------------------

Assuming:
 * Java 8 is installed
 * Port 8080 is empty , if not please change server.port property in `application.properties` in `src/main/resources`
 * MySQL is installed on the machine on port 3306(default port), if not please change port in `application.properties`


Features:
-----------------------------
-Entity generation: Define entities with fields and data types, and the generator will create corresponding Java classes and database tables.
-Enum generation: Define enums with values, and the generator will create corresponding Java enums.
-Relationship mapping: Define relationships between entities, and the generator will handle the necessary JPA mappings.
-REST API generation: Automatically generate RESTful APIs for CRUD operations on entities.

Running the application
-----------------------------
Follow these steps to run this project:

#### Build : 
To build the application `mvn clean install`

#### Run : 

Run the application using `mvn spring-boot:run`

#### Release : 
To release a production version `mvn release:clean release:prepare`

#### Deploy :
To deploy build on nexus  `mvn clean deploy`




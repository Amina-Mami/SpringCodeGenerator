# CodeGen backend
CodeGen backend project.

Assumptions:
-----------------------------

Assuming:
 * Java 8 is installed
 * Port 8080 is empty , if not please change server.port property in `application.properties` in `src/main/resources`
 * MySQL is installed on the machine on port 3306(default port), if not please change port in `application.properties`


APIs:
-----------------------------
- create project - api

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

#### Deployment Script
```
applicationLoc="/Users/deepak/projects/codegen/target/"

propertyLoc="/Users/deepak/projects/codegen/application-prod.properties"

portNumber="8010"

port=$(netstat -ntpln | grep $portNumber | awk -F '/java' '{print $1}' | awk -F'LISTEN' '{print $2}' | awk '{ gsub (" ", "", $0); print}')

echo "Killing pid"

kill -9 $port

echo "application Name : $applicationLoc"

cd /Users/deepak/projects/codegen
mvn clean install -DskipTests=true
cd target/
pwd
echo "current location is "
pwd
cd $applicationLoc

jarName="codegen-0.0.1-SNAPSHOT.jar"

echo "Jar Name $jarName"

echo "executing : nohup java  application-prod.properties -jar $applicationLoc$jarName --spring.config.location=file:$propertyLoc >> /Users/deepak/projects/applicationlog/codegen.log &"

#nohup java -Xmx1024m -Xms512m -jar $applicationLoc$jarName --spring.config.location=file:$propertyLoc > /dev/null &
```
**Note**:  
   

Exception Handling
-----------------------------
This project uses global exception handling using `ResponseEntityExceptionHandler` check `ExceptionInterceptor` class for reference.


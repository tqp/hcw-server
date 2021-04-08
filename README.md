# Tim's Analytics App Server

## Running the server.
- Create an environment called "Build App" with the following settings:
    - Command Line: clean install -f pom.xml
- To Debug:
    - 

## Notes
netstat -ano | findstr <port>

#### Tomcat Environment Config
Create/update /tomcat/bin/setenv.bat.  
```text
echo "Running setenv.bat"
set JAVA_OPTS=%JAVA_OPTS% -Dspring.profiles.active=dev -agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n
```

#### Oracle Query Note
To change the order of columns within a table:
```text
create table
    newTable
as
select
    col3,
    col2,
    col1
from
    table;
rename table to oldTable;
rename newTable to table;
```

#### Spring Profiles Active
To set "spring.profiles.active" the way you would when starting Tomcat, open ElasticBeanstalk, 
go to Configuration -> Software -> Environment Properties. Then, set:  
```text
name = spring.profiles.active, value = prod
```
  
Don't get fooled by all the websites that say to set SPRING_PROFILES_ACTIVE.

#### AWS Health Checks on HTTPS

Ref: https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/configuring-https-httpredirect.html

"If your environment uses Elastic Load Balancing health checks, the load balancer expects a healthy instance to respond to the HTTP health check messages with HTTP 200 (OK) responses. Therefore, your web server shouldn't redirect these messages to HTTPS. The example configuration files in https-redirect handle this requirement correctly."

You have to add the following file to your Java server:  
dd

#### Installing the external JAR
```
mvn install:install-file -Dfile=C:\Users\tqp\.m2\repository\com\timsanalytics\jar\tqp-auth-framework\1.0.0 -DgroupId=com.timsanalytics.jar -DartifactId=tqp-auth-framework -Dversion=1.0.0=true
```
Then, add the following line to Application.java
```
@ComponentScan(basePackages = {"com.timsanalytics.jar"})
```

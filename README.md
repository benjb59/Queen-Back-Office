![Build](https://github.com/InseeFr/Queen-Back-Office/actions/workflows/release.yml/badge.svg)

# Queen-Back-Office
Back-office services for Queen  
REST API used for communication with Queen/Stromae UI

## Requirements
For building and running the application you need:
- Java 21
- Maven 3  

## Install and execute unit tests
Use the maven clean and maven install 
```shell
mvn clean install
```  

## Running the application locally
Use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:  
```shell
mvn spring-boot:run
```  

## Database
Queen-Back-Office uses postgresql as data source
 
## Deployment
### 1. Package the application
```shell
mvn clean package
```  
The jar will be generated in `/target` repository  

### 2. Launch app with embedded tomcat
```shell
java -jar app.jar
```

### 3. Application Access
To access the swagger-ui, use this url : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Docker/Kubernetes

A Dockerfile is present on this root project to deploy a container. You can [get docker images on docker hub](https://hub.docker.com/r/inseefr/queen-back-office/tags)

[Helm chart repository](https://github.com/InseeFr/Helm-Charts/) is available for the queen backoffice/db/frontend


## Liquibase
Liquibase is enabled by default and run changelogs if needed.

### Generate diff changelog between twos databases
```shell
# Don't forget to edit configuration properties in pom.xml for this
mvn liquibase:diff
```

#### Properties
Minimal configuration for dev purpose only (there is no auth in this configuration)
User is considered as authenticated admin user

```yaml  
application:
  corsOrigins: https://test.com #mandatory
  # define folder where temp files will be created
  # /!\ do not use the OS default temp folder or java.io.tmpdir as it causes security issues
  # give only access rights to this folder to the user executing the tomcat process 
  temp-folder: /opt/app/app-temp
  auth: NOAUTH
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/defaultSchema
    username: 
    password: 
    driver-class-name: org.postgresql.Driver
  jpa:
    show-sql: true
  jackson:
    serialization:
      indent_output: true
logging:
  file:
    name: /path/to/log/folder/queen.log
  level:
    root: INFO
feature:
  enable:
    # enable data loading in DB 
    dataset: true
    # enable swagger
    swagger: true
    # enable pilotage api 
    pilotage: false
    # enable cache
    cache: true
    # enable comments endpoints
    comments: false
    # enable interviewer endpoints 
    interviewer-collect: false
```

Configuration example with OIDC and pilotage api enabled

```yaml 
application:
  corsOrigins: https://test.com # mandatory
  # define folder where temp files will be created
  # /!\ do not use the OS default temp folder or java.io.tmpdir as it causes security issues
  # give only access rights to this folder to the user executing the tomcat process 
  temp-folder: /opt/app/app-temp
  auth: OIDC
  roles:
    interviewer: interviewer_role
    reviewer: reviewer_role
    admin: admin_role
    webclient: webclient_role
    reviewer-alternative: reviewer_alternative_role
  pilotage:
    # url used to check habilitations for a user 
    url: https://pilotage-api.com
  security:
    # if oidc auth enabled
    oidc:
      auth-server-host: https://auth.host
      client-id: clientId
      realm: realmUsed
      principal-attribute: username
      role-claim: role_claim
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/defaultSchema
    username: 
    password: 
    driver-class-name: org.postgresql.Driver
  jpa:
    show-sql: true
logging:
  file:
    name: /path/to/log/folder/queen.log
  level:
    root: INFO
feature:
  enable:
    # enable data loading in DB 
    dataset: true
    # enable swagger
    swagger: true
    # enable pilotage api 
    pilotage: true
    # enable cache
    cache: true
    # enable comments endpoints
    comments: false
    # enable interviewer endpoints 
    interviewer-collect: false
```

## End-Points
- Campaign
	- `GET /campaigns` : Retrieve the campaigns the current user has access to
    - `GET /admin/campaigns` : Retrieve all campaigns
	- `POST /campaigns` : Create a new campaign
	- `POST /campaign/context` : Integrate a full campaign (campaign/nomenclatures/questionnaires. The results of the integration indicate the successful/failed integrations
	- `DELETE /campaign/{id}` : Delete a campaign
	

- Questionnaire
	- `GET /questionnaire/{id}` : Retrieve the data structure of a questionnaire
	- `GET /campaign/{id}/questionnaires` : Retrieve the data structure of all questionnaires linked to a campaign
	- `GET /campaign/{idCampaign}/questionnaire-id` : Retrieve all the questionnaire ids for a campaign
	- `POST /questionnaire-models` : Create a new questionnaire

- SurveyUnit
	- `GET /survey-units` : Retrieve all survey units id 
	- `GET /survey-unit/{id}` : Retrieve the survey unit
	- `GET /survey-unit/{id}/deposit-prof` : Generate and retrieve a deposit proof (pdf file) for a survey unit
	- `GET /campaign/{id}/survey-units` : Retrieve all the survey units of a campaign
	- `PUT /survey-unit/{id}` : Update a survey unit
    - `GET /survey-units/interviewer` : Retrieve all the survey units of the current interviewer
	- `POST /campaign/{id}/survey-unit` : Create or update a survey unit
	- `DELETE /survey-unit/{id}` : Delete a survey unit

- Data
	- `GET /survey-unit/{id}/data` : Retrieve the data of a survey unit
	- `PUT /survey-unit/{id}/data` : Update the data of a survey unit

- Comment
	- `GET /survey-unit/{id}/comment` : Retrieve the comment of a survey unit
	- `PUT /survey-unit/{id}/comment` : Update the comment of a survey unit

- Nomenclatures
	- `GET /questionnaire/{id}/required-nomenclatures` : Retrieve all required nomenclatures of a questionnaire
    - `GET /campaign/{id}/required-nomenclatures` : Retrieve all required nomenclatures of a campaign
	- `GET /nomenclature/{id}` : Retrieve the json value of a nomenclature
	- `POST /nomenclature` : Create/update a nomenclature
    - `GET /nomenclatures` : Retrieve all nomenclatures ids

- Personalization
	- `GET /survey-unit/{id}/personalization` : Retrieve the personalization of a survey unit
	- `PUT /survey-unit/{id}/personalization` : Update the personalization of a survey unit
	
- Metadata
	- `GET /campaign/{id}/metadata` : Retrieve campaign metadata
	- `GET /questionnaire/{id}/metadata` : Retrieve campaign metadata

- Paradata
	- `POST /paradata` : Create a paradata event for a survey unit
	
- StateData
	- `GET /survey-unit/{id}/state-data` : Retrieve the state-data of a survey unit
	- `PUT /survey-unit/{id}/state-data` : Update the state-data of a survey unit
	- `POST /survey-units/state-data` : Get state-data for all survey-units defined in request body
	
- DataSet
	- `POST /create-dataset` : Create dataset for demo environments

## Libraries used
- spring-boot-data-jpa
- spring-boot-security
- spring-boot-web
- spring-boot-tomcat
- spring-boot-test
- spring-boot-starter-oauth2-resource-server
- liquibase
- postgresql
- junit
- springdoc
- caffeine (cache)

## License
Please check [LICENSE](https://github.com/InseeFr/Queen-Back-Office/blob/main/LICENSE) file

# Introduction
The Stock Quote Application is a Java-based application that allows users to fetch stock
 quotes from an external API (Alpha Vantage REST API), store them in a PostgreSQL
 database, and simulate a simple stock wallet where users buy and sell stocks.

The Java application leverages Maven for various tasks such as building the project,
 managing dependencies, enforcing project structure, and cleaning artifacts. IntelliJ
 IDEA serves as the integrated development environment (IDE) for coding, running, and
 debugging the application. Java Database Connectivity (JDBC) connects the application
 to the PostgreSQL database. Additionally, Docker is utilized to create a Docker image
 for the application, which is then pushed to Docker Hub for distribution and deployment.
 Finally, JUnit and Mockito are used for automated testing.

# Quick Start
First, pull the Docker image from Docker Hub:
```docker pull tanyanantsa/stockquote:latest```
Run the Container instance:
```docker run --rm -it -v `pwd`/log:/log tanyanantsa/stockquote```

# Implementation
## ER Diagram
![ER Diagram](./stockquoteERD.png "stockquoteERD.png")

## Design Patterns
In the Stock Quote Application, two important design patterns are used: DAO and
 Repository. The DAO pattern facilitates the interaction with the database and allows
 for data access without worrying about the business details of how it's implemented.
 The application has DAO classes like QuoteDao and PositionDao, which handle tasks
 like saving, retrieving, updating, and deleting data from the PostgreSQL database.

The Repository pattern works hand in hand with DAOs to make database operations more
 aligned with the way we think about our application's data. Instead of directly dealing
 with database logic, the application uses classes like QuoteService and PositionService.
 These classes act as a bridge between our code and the database, making it easier to
 manage complex database operations and keeping our code organized and easier to maintain.

# Test
The database was set up with the name stock_quote, and tables were created according to
 the ER Diagram included above. To test the implemented CRUD class for each table, I
 designed unit tests for both the QuoteDao and PositionDao class named QuoteDao_Test
 and PositionDao_Test. I wrote unit tests, which implemented JUnit, that did not mock
 the JBDC connection. For each test, test data was created and inserted into the test
 database. This data included various stock quotes and positions. After executing specific
 operations or queries against the database, the results were verified to ensure they
 matched the expected outcome. The application was tested under various scenarios,
 including adding, updating, and deleting records, and handling invalid inputs.  
 
The integration tests, QuoteService_Int and PositionService_Int, were implemented to test
 the service layer of the application and the end-to-end application flow. Both tests
 sets up a connection to a local PostgreSQL database and verifies that each classes' methods
 return the expected result when given sample data.  
 
The unit tests, QuoteService_Unit and PositionService_Unit, were designed to evaluate
 the service layer of the application isolated from it's dependencies. These test
 utilize Mockito to mock the behaviour of the data access layer of the application and
 validate that the methods of each class produce the anticipated outcome when provided
 with sample data.  

# Deployment
To enable easy distribution and deployment, this application has been Dockerized.
 The steps below explain how I built the Docker image:

### Package the Java app
```mvn clean package```

### Build a new Docker image locally
```docker build -t tanyanantsa/stockquote .```

### Push the image to Docker Hub
```docker push tanyanantsa/stockquote```

# Improvement
- Improve the UI and implement a graphical user interface (GUI) to enhance the user
 experience.

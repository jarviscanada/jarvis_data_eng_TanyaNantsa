# PySpark Fundamentals Project

## About the Project

The goal of this project was to learn Spark fundamental concepts and work with the Databricks platform. This project involved completing data manipulation exercises and writing several ETL jobs that use various data sources including CSV files, tables, partitioned files, APIs (using HTTP requests), and RDBMS.  
The project was written in Python using Apache Spark. Databricks notebooks were used to write the code, and these notebooks were subsequently uploaded to GitHub.  

## Project Structure

- **Data Manipulation Exercises**: Practiced various data transformation techniques using PySpark.
- **ETL Jobs**: Implemented ETL jobs to extract, transform, and load data from multiple sources.

## ETL Jobs Overview

### Job 1: Parquet File Optimization

- **Objective**: Load data into a Parquet file to speed up reading and writing for large datasets.
- **Optimization**: Used partitions when loading data to tables to improve query performance.

### Job 2: API/HTTP Requests

- **Objective**: Extract stock data from the Alpha Vantage API.
- **Process**:
  - Sent HTTP requests to the API using the `requests` library in Python.
  - Converted fetched data to a DataFrame and performed transformations.
  - Calculated weekly max closing prices for each company.
  - Partitioned the DataFrame by company and loaded it into a managed table.

### Job 3: RDBMS Integration

- **Objective**: Extract data from a PostgreSQL database.
- **Process**:
  - Defined the JDBC connection properties (URL, user, password, driver).
  - Read data from RDBMS into a DataFrame using `spark.read.jdbc`.
  - Performed transformations and loaded the data into a managed table.

### Job 4: CSV Files/Tables

- **Objective**: Extract data from CSV files and managed tables.
- **Process**:
  - Loaded data from CSV files into DataFrames using `read.csv`.
  - Performed transformations and loaded data into managed tables using `saveAsTable`.

## Optimizations

- **Columnar File Format**: Used Parquet for storing transformed data, which is optimized for read-heavy operations.
- **Partitions**: Implemented partitions to enhance query performance.
- **Efficient Aggregations**: Grouped data and calculated aggregations to ensure efficient data processing.

## Key Concepts

- **Columnar File Format**: Used Parquet for efficient data storage and retrieval.
- **Partitions**: Improved query performance by partitioning data.
- **API/HTTP Requests**: Extracted and transformed data from external APIs.
- **RDBMS Integration**: Connected to and extracted data from a PostgreSQL database.
- **DataFrames**: Utilized DataFrames for data manipulation and transformation.

## Challenges Faced

- **Troubleshooting Issues:** Identifying and resolving issues during development and testing, including debugging complex Spark jobs and transformations.
  - **Solution:** Validating results to ensure that transformations and aggregations are correct.
- **Configuration Issues:** Configuring JDBC connections correctly, including handling connection properties and driver compatibility.
  - **Solution:** Ensuring secure handling of credentials and connection details.


## Possible Improvements

- **Query Optimization:** Refine Spark queries to improve performance, such as using partitioning, Parquet, indexing, and optimizing join operations.

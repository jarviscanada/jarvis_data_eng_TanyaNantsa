# Introduction
## Purpose of this Project
The data analytics team previously used SAP and R for data processing but aims to transition to big data platforms. This project uses Apache Hadoop to facilitate this transition.  

I explored and evaluated core Hadoop components such as MapReduce, HDFS, and YARN. Additionally, I have provisioned a Hadoop Cluster using Google Cloud Platform (GCP) and worked on solving business problems using Apache Hive and Zeppelin Notebook.  

Cluster Architecture: 1 master and 2 worker nodes. Includes HDFS, YARN, Zeppelin, Hive (Hive Server, Hive Metastore, RDBMS), etc.  

Big Data Tools: MapReduce, YARN, HDFS, Hive, Zeppelin.  

# Hive Project
## Optimization of Hive Queries
Partitioning by Year: I partitioned data by year in Hive tables, which allowed for more efficient data retrieval when querying specific time periods. For example, partitioning by year enabled faster aggregation and analysis of yearly trends in GDP growth and other metrics.  
Storing as Parquet: I stored Hive tables using the Parquet format, which is column-oriented. This format reduces I/O operations and minimizes storage space while improving query performance significantly. It also supports efficient predicate pushdown, enabling Hive to skip unnecessary data during query execution.

## Zeppelin Notebook


# Improvements
- Continuously optimize Hive queries by reviewing execution plans and leveraging techniques such as indexing where applicable.
- Implement dynamic resource allocation for Hive jobs to optimize performance based on workload demands.

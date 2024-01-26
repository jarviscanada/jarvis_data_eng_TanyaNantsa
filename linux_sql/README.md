# Linux Cluster Monitoring Project
The Linux cluster monitoring project is designed to resolve the Jarvis Linux Cluster Administration (LCA) team’s need to manage a Linux cluster of 10 servers running CentOS 7.
The project collects and stores real-time hardware specifications and resource usage data for each server, enabling the LCA team to generate reports and make informed decisions about server additions or removals.

The technologies used include Docker for the containerized deployment of the Postgres instance, Bash scripts for the data collection on individual servers, Postgres for the database, and Git for version control and feature management within the project.


# Quick Start
- Start a psql instance using psql_docker.sh
    `./scripts/psql_docker.sh start`
- Create tables using ddl.sql
    `psql -h localhost -U postgres -d host_agent -f sql/ddl.sql`
- Insert hardware specs data into the DB using host_info.sh
    `./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password`
- Insert hardware usage data into the DB using host_usage.sh
    `bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password`
- Crontab setup
    `crontab -e`
- In the crontab editor insert code written below. Ensure you add the correct full file location for the host_usage script:
    `* * * * * bash /[insert full file path for host_usage script]/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log`

# Implementation
A Postgres instance was set up using Docker to serve as the database. Then, two tables were created to store the resource usage and hardware specification data in the Postgres instance. The hardware specifications and resource usage information are collected using bash scripts that run on every server. The scripts also direct the data into the Postgres instance. Finally, the monitoring project, deployed across all servers, collects data every minute, storing it in the Postgres database. 

## Architecture
![Cluster Diagram](linux_sql/assets/cluster_diagram.drawio.png)

## Scripts
### psql_docker.sh
The psql_docker.sh script manages a PostgreSQL Docker container, allowing users to create, start, or stop the container based on the specified command-line arguments. The script uses a switch case to handle different commands.
- Usage:
    `./scripts/psql_docker.sh start|stop|create [db_username][db_password]`
    `./scripts/psql_docker.sh create db_username db_password`
    `./scripts/psql_docker.sh start`
    `./scripts/psql_docker.sh stop`

### host_info.sh
The host_info.sh script automates collecting hardware information from a Linux machine and inserting it into a PostgreSQL database. Hardware specifications are parsed using Bash commands and assigned to variables. An INSERT statement is built based on the parsed hardware information to add data to the database.
- Usage:
    `bash scripts/host_info.sh psql_host psql_port db_name psql_user psql_password`
    
### host_usage.sh
The host_usage.sh script automates collecting resource usage data from a Linux machine and inserting it into a PostgreSQL database. Resource usage data is parsed using Bash commands and assigned to variables. An INSERT statement is built based on the resource usage information to add data to the database.
- Usage:
    `bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password`

### crontab
The crontab script executes the host_usage.sh script every minute to continuously collect resource usage data.
- Usage:
    `crontab -e`
    `crontab -l`

## Database Modeling
`host_info`
| Column        | Type      | Constraints                |
|---------------|-----------|----------------------------|
| id            | SERIAL    | PRIMARY KEY                |
| hostname      | VARCHAR   | UNIQUE                     |
| cpu_number    | INT2      |                            |
| cpu_architecture | VARCHAR |                            |
| cpu_model     | VARCHAR   |                            |
| cpu_mhz       | FLOAT8    |                            |
| l2_cache      | INT4      |                            |
| timestamp     | TIMESTAMP |                            |
| total_mem     | INT4      |                            |


`host_usage`
| Column          | Type      | Constraints                           |
|-----------------|-----------|---------------------------------------|
| timestamp       | TIMESTAMP |                                       |
| host_id         | SERIAL    | FOREIGN KEY (host_id) REFERENCES host_info(id) |
| memory_free     | INT4      |                                       |
| cpu_idle        | INT2      |                                       |
| cpu_kernel      | INT2      |                                       |
| disk_io         | INT4      |                                       |
| disk_available  | INT4      |                                       |


# Test
The bash scripts were tested on my own Linux machine running CentOS. The bash scripts were run manually. The scripts performed as expected without issue.

# Deployment
The app was deployed using crontab. Crontab automates host_usage.sh script and runs it every minute. All the files ready for deployment are merged to the Main branch in Github.

# Improvements
- Handle hardware updates 
- Implement more robust error handling for incorrect inputs
- Write functions to modularize repeated code

# Introduction
This project aims to analyze customer shopping behavior for London Gift Shop (LGS), a UK-based online store. By Using data analytics, LGS wants to understand its customers better and develop targeted marketing techniques to increase revenue.

The analytic results will be used by LGS to develop sales and marketing strategies, such as personalized email campaigns, targeted promotions, and event planning. These strategies will be designed to attract new customers and retain existing ones, thereby driving growth and increasing revenue for the company.

For this project, I utilized Jupyter Notebook along with Python libraries such as Pandas and NumPy for data analytics and wrangling. The provided transaction data, extracted from the LGS database, was analyzed to uncover insights into customer behavior and preferences.

# Implementation
## Project Architecture
The architecture of this project involves a front end stack consisting of CDN (Content Delivery Network), Azure Blob Storage, HTML/CSS, and JavaScript for the LGS web app. The backend stack includes API management for handling requests, an AKS cluster (microservices) for application deployment, and an Azure SQL Server OLTP (Online Transaction Processing) database for managing customer transaction data. The customer transaction data, initially stored in a SQL file, was sent to a PostgreSQL data warehouse (OLAP - Online Analytical Processing) for analytics purposes. Data analytics are performed in a Jupyter Notebook, where insights are extracted from the transaction data to inform business decisions and marketing strategies.

<!-- ![Project Architecture Diagram](./project_architecture_diagram.png) -->

## Data Analytics and Wrangling
[Link to Jupyter Notebook:](./python_data_wrangling/retail_data_analytics_wrangling.ipynb)

The data analytics and wrangling process involved cleaning and processing the transaction data to extract meaningful insights. By analyzing customer purchase patterns, frequency of purchases, popular products, and customer segmentation, we aimed to provide actionable recommendations to LGS for increasing revenue.

Some strategies that could be implemented based on the data include:
- Identifying high-value customers and offering them personalized promotions or discounts.
- Recommending related products to customers based on their purchase history.
- Analyzing seasonal trends to plan targeted marketing campaigns and promotions.
- Optimizing inventory based on product popularity and demand.

# Improvements
If given more time, the following improvements could be implemented:
1. Implementing predictive analytics models to forecast future sales and customer behavior.
2. Integrating real-time data analytics to provide up-to-date insights for decision-making.
3. Conducting A/B testing of different marketing strategies to measure their effectiveness and optimize performance.

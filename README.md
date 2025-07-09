# ? Bookstore API Project

This project provides a RESTful backend for a simple bookstore system. The application is built using Spring Boot and is designed for rapid testing and development, without authentication constraints.

## ? API Access

Once the server is running, the REST APIs are accessible locally through Swagger-UI at the following URL:

[http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

Swagger provides a friendly interface for exploring and testing available endpoints.

## ? Security

?? **Security is completely disabled in this application.**  
There is no authentication mechanism in place, allowing unrestricted access to all endpoints. This is intentional for ease of use in a local development and demonstration context.

## ?? Database

The backend uses **PostgreSQL** as its relational database.

- A **backup file** of the database is included in the project directory, alongside this `README.md`.
- Before running the backup, make sure to create the necessary PostgreSQL user using the credentials specified in `application.yml`.

### ? Required User Setup

Create a PostgreSQL user named `bookstoreadmin` with the appropriate privileges and encrypted password:

```sql
-- Role: bookstoreadmin
-- DROP ROLE IF EXISTS bookstoreadmin;

CREATE ROLE bookstoreadmin WITH
  LOGIN
  SUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  NOREPLICATION
  NOBYPASSRLS
  ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:ybrf9uNsA5g+RSN+VX1gkA==$co+QVO9gKqv2VSvWiVf9vNH3qfL0DUy+S4Mw8E25Ac4=:VpnciUzCBDqps/3T9Wxj23CrMBDlL3N/ZlogBJ6NNaI=';

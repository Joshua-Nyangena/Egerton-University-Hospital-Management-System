# Egerton University Hospital Management System (EUHMS)

A full stack hospital management system built with Java and Spring Boot. It manages patient records and appointments, and protects every action with secure login and role based access control.

I designed and built this project on my own, from the database schema to the REST APIs and security layer.

## Why I built it

Hospitals depend on accurate records and the right people seeing the right data. I wanted to build a system that handles that responsibly: patient information should only be visible to staff who are allowed to see it, and every request should be checked. This project let me practise secure backend design, relational database work and clean API design in one realistic system.

## Features

* **Patient records:** register patients and store, view and update their details.
* **Appointments:** book, view and manage appointments between patients and staff.
* **Secure authentication:** users sign in and receive a JSON Web Token (JWT) that is checked on every protected request.
* **Role based access control:** what a user can see or do depends on their role.
* **RESTful APIs:** documented endpoints that follow standard HTTP conventions.
* **Relational database:** data stored in MySQL and retrieved with advanced SQL queries.

## Tech stack

| Area | Technology |
| --- | --- |
| Language | Java |
| Framework | Spring Boot |
| Security | Spring Security with JWT |
| Database | MySQL |
| Build tool | Maven (Maven Wrapper included) |
| API style | REST |
| License | MIT |

## How security works

1. A user sends their credentials to the login endpoint.
2. The server checks the credentials and returns a signed JWT.
3. For every later request, the client sends the token in the `Authorization: Bearer <token>` header.
4. A security filter validates the token and loads the user and their role.
5. Each endpoint then allows or rejects the request based on that role.

Requests with a missing, expired or invalid token are rejected.

## Roles

EDIT THIS SECTION to match your project. List each role and what it can do, for example:

| Role | What this role can do |
| --- | --- |
| EDIT: role name | EDIT: for example, manage users and view all records |
| EDIT: role name | EDIT: for example, view patients and manage appointments |
| EDIT: role name | EDIT: for example, view their own appointments |

## API overview

EDIT THIS TABLE with your real endpoints. The method, path and purpose are enough.

| Method | Endpoint | Purpose | Access |
| --- | --- | --- | --- |
| POST | EDIT: login path | Sign in and receive a JWT | Public |
| GET | EDIT: patients path | List patients | EDIT: roles |
| POST | EDIT: patients path | Register a patient | EDIT: roles |
| GET | EDIT: appointments path | List appointments | EDIT: roles |
| POST | EDIT: appointments path | Book an appointment | EDIT: roles |

## Project structure

```
Egerton-University-Hospital-Management-System
  src/main/java        Application code (controllers, services, repositories, security)
  src/main/resources   Configuration files
  pom.xml              Maven dependencies and build settings
  mvnw, mvnw.cmd       Maven Wrapper (no separate Maven install needed)
  LICENSE              MIT license
```

## Getting started

### Prerequisites

* Java JDK (check `pom.xml` for the version the project uses)
* MySQL Server running on your machine
* Git

### 1. Clone the repository

```
git clone https://github.com/Joshua-Nyangena/Egerton-University-Hospital-Management-System.git
cd Egerton-University-Hospital-Management-System
```

### 2. Create the database

Open MySQL and create an empty database. Use the same name that appears in your configuration file.

```
CREATE DATABASE EDIT_DATABASE_NAME;
```

### 3. Configure the application

Open `src/main/resources/application.properties` and set your own values for:

* the database URL
* the database username and password
* the JWT secret key

Do not commit real passwords or secrets to GitHub. Keep them on your own machine.

### 4. Run the application

On Windows:

```
mvnw.cmd spring-boot:run
```

On Linux or macOS:

```
./mvnw spring-boot:run
```

The server starts on the port set in your configuration (Spring Boot uses 8080 by default).

### 5. Try the API

Use Postman or a similar tool. First call the login endpoint to get a token, then send that token in the `Authorization: Bearer <token>` header when calling protected endpoints.

## Screenshots

Add screenshots here once you have them. For example:

* Login request and token response in Postman
* A protected endpoint returning data with a valid token
* The same endpoint returning an error without a token

## What I learned

* Building secure authentication with Spring Security and JWT
* Designing role based access so each user only reaches what they should
* Designing a relational schema and writing advanced SQL queries in MySQL
* Structuring a Spring Boot project into clear layers
* Designing and documenting REST APIs

## Possible improvements

* A billing module linked to patients and appointments
* Automated tests for the services and security rules
* Docker setup so the app and database start with one command
* A web frontend for staff and patients
* Deployment to a cloud host

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Author

**Joshua Nyangena**
Computer Science graduate, Egerton University
GitHub: https://github.com/Joshua-Nyangena

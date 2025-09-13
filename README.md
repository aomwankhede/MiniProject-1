# PMS (Payment Management System) - Setup Guide

## 1. Clone the Repository
```bash
git clone https://github.com/aomwankhede/MiniProject-1
```
## 2. Database Setup (PostgreSQL)
```bash
-- Create Database
  CREATE DATABASE pms;
  
-- Connect to Database
  \c pms;
  
-- Create Tables
    CREATE TABLE permission (
        id BIGSERIAL PRIMARY KEY,
        target VARCHAR(100) NOT NULL,
        action VARCHAR(20) NOT NULL
    );
    
    CREATE TABLE role (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(50) NOT NULL,
        description TEXT,
        permission_id BIGINT NOT NULL,
        CONSTRAINT fk_role_permission FOREIGN KEY (permission_id) REFERENCES permission(id)
    );
    
    CREATE TABLE users (
        id BIGSERIAL PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        email VARCHAR(100) NOT NULL UNIQUE,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        role_id BIGINT NOT NULL,
        CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(id)
    );
    
    CREATE TABLE client (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(100),
        bank_account VARCHAR(50),
        contact_email VARCHAR(100),
        company VARCHAR(100),
        contract_id VARCHAR(50)
    );
    
    CREATE TABLE vendor (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(100),
        bank_account VARCHAR(50),
        contact_email VARCHAR(100),
        gst_number VARCHAR(30),
        invoice_terms TEXT
    );
    
    CREATE TABLE employee (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(100),
        bank_account VARCHAR(50),
        contact_email VARCHAR(100),
        department VARCHAR(50),
        pan_number VARCHAR(20)
    );
    
    CREATE TABLE client_payment (
        id BIGSERIAL PRIMARY KEY,
        amount NUMERIC(12,2) NOT NULL,
        direction VARCHAR(10) NOT NULL CHECK (direction IN ('IN', 'OUT')),
        status VARCHAR(20) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        client_id BIGINT NOT NULL REFERENCES client(id)
    );
    
    CREATE TABLE salary_payment (
        id BIGSERIAL PRIMARY KEY,
        amount NUMERIC(12,2) NOT NULL,
        direction VARCHAR(10) NOT NULL CHECK (direction IN ('IN', 'OUT')),
        status VARCHAR(20) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        employee_id BIGINT NOT NULL,
        CONSTRAINT fk_salary_payment_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
    );
    
    CREATE TABLE vendor_payment (
        id BIGSERIAL PRIMARY KEY,
        amount NUMERIC(12,2) NOT NULL,
        direction VARCHAR(10) NOT NULL CHECK (direction IN ('IN', 'OUT')),
        status VARCHAR(20) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        vendor_id BIGINT NOT NULL,
        CONSTRAINT fk_vendor_payment_vendor FOREIGN KEY (vendor_id) REFERENCES vendor(id)
    );
```

## 3. Create a admin user
1) Insert all permissions in the permission table.

2) Create a role (e.g., ADMIN) and assign all permissions to it.

3) Create a user in the users table and assign the ADMIN role.

## 4. Edit application.properties file in src/main/resources
```bash
db_class_name =
db_database_url =
db_username=
db_password=
db_database_name=
```

## 5. Execute mvn package command in the root directory
```bash
mvn package
```

## 6. Run the jar file that is created in the target folder
```bash
java -jar filename.jar
```

## 7. You can directly run the main class no need of mvn
### Option 1: Using JAR
```bash
java -jar target/your-jar-file.jar
```
### Option 2: Run Main Class Directly
```bash
You can also run the main class from your IDE without packaging.
```

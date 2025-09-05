# Hospital Management System

This project is a backend solution for a Hospital Management System developed with Spring Boot.  It provides RESTful APIs to manage patients, departments, admissions, discharges, and clinical records. The system follows the repository pattern and is built using a layered architecture (Controller → Service → Repository) with DTOs and entities.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Service Layers](#service-layers)
    - [Department Service Layer](#department-service-layer)
    - [Patient Service Layer](#patient-service-layer)
    - [Admission State Service Layer](#admission-state-service-layer)
    - [Clinical Data Service Layer](#clinical-data-service-layer)
- [Business Specific APIs](#business-specific-apis)
- [Unit Tests](#unit-tests)
    - [Service Layer Unit Tests](#unit-tests-across-service-layers)
    - [Business API Unit Tests](#unit-tests-for-business-specific-apis)
- [UML Diagrams](#uml-diagrams)
- [Tech Stack](#tech-stack)
- [How to Run](#how-to-run)

---

## Project Overview

This project is a backend solution for a Hospital Management System developed with Spring Boot.  It provides RESTful APIs to manage patients, departments, admissions, discharges, and clinical records. The system follows the repository pattern and is built using a layered architecture (Controller → Service → Repository) with DTOs and entities.

---

##  Architecture

The system is built on a **layered architecture**:

- **Controller Layer** → Handles HTTP requests & responses.
- **Service Layer** → Business logic (tested with 57 unit tests).
- **Repository Layer** → Data persistence using **Spring Data JPA**.
- **DTOs & Entities** → Data transfer between layers.

---

## Service Layers

### Department Service Layer
- `GET /department` → Find all departments
- `GET /department/{id}` → Find one department
- `POST /department` → Create department
- `PATCH /department/{id}` → Update department
- `DELETE /department/{id}` → Delete department
- `GET /department/searchDepartment?departmentName` → Search by name

### Patient Service Layer
- `GET /patient` → Find all patients
- `GET /patient/{id}` → Find one patient
- `POST /patient` → Create patient
- `PATCH /patient/{id}` → Update patient
- `DELETE /patient/{id}` → Delete patient
- `GET /patient/searchPatient?patientFullName` → Search by name

### Admission State Service Layer
- `GET /admissionState` → Find all admission states
- `GET /admissionState/{id}` → Find one admission state
- `POST /admissionState` → Create admission state
- `PATCH /admissionState/{id}` → Update admission state
- `DELETE /admissionState/{id}` → Delete admission state
- `GET /admissionState/searchByPatient?patientId` → Search by patient

### Clinical Data Service Layer
- `GET /clinicalData` → Find all clinical data
- `GET /clinicalData/{id}` → Find one clinical data
- `POST /clinicalData` → Create clinical data
- `PATCH /clinicalData/{id}` → Update clinical data
- `DELETE /clinicalData/{id}` → Delete clinical data
- `GET /clinicalData/searchByPatient?patientId` → Search by patient

---

## Business Specific APIs

- **Discharge Patient**
    - `PATCH /discharge/{admission_state_id}/{reason}`
    - Marks patient as discharged, assigns exit date & reason.

- **Get Department Change History**
    - `GET /departmentChangeHistory/searchByPatient/{patientId}`
    - Retrieves complete department change history with pagination.

---

## Unit Tests

### Unit Tests Across Service Layers
- **Create** → Verifies entity creation.
- **Find All** → Ensures correct pagination results.
- **Find One** → Verifies retrieval by ID.
- **Update** → Ensures correct field updates.
- **Delete** → Validates deletion conditions.
- **Search** → Ensures repositories return correct data.

### Unit Tests for the Business Specific APIs
- **Discharge Reason** → Validates discharge process & reason tracking.
- **Department Change History** → Ensures department changes are tracked with old/new dept and dates.

---

## UML Diagrams

- Patient UML Diagram  
  ![Patient UML](src/docs/PatientUML.png)

- Department UML Diagram  
  ![Department UML](src/docs/DepartmentUML.png)

- Entities UML Diagram  
  ![Entities UML](src/docs/EntitiesUML.png)

---

## Tech Stack

- **Backend**: Spring Boot (Java)
- **Database**: PostgreSQL (running in a **local Docker container**)
- **ORM**: Spring Data JPA (Hibernate)
- **Testing**: JUnit, Mockito
- **Build Tool**: Maven/Gradle
- **Architecture**: Repository Pattern, DTOs & Entities

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/rigelHadushiDev/hospitalManagement.git
   cd hospitalManagement
   ```
2. Ensure PostgreSQL is running in a local Docker container.
3. Build and run the project:
   ```bash
   mvn spring-boot:run
   ```
4. Access the APIs:
   ```bash
   http://localhost:8080
   ```
5. Run unit tests:
   ```bash
    mvn test
    ```
   
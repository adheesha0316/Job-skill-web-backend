# 💼 Job-skill-web-backend

A robust backend system for the **Job Skill Platform**, designed to connect job seekers, employers, and trainers. This service provides secure APIs to manage job listings, applications, user accounts, and skill development courses.

## 🚀 Overview

The platform empowers users to:
- Apply for jobs
- Post and manage job vacancies
- Offer and enroll in courses
- Maintain secure user authentication with role-based access

## 🛠️ Built With

- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **Hibernate & JPA**
- **MySQL** (or PostgreSQL)
- **Maven**

## 🧩 Key Features

- 🔐 JWT-based Authentication & Authorization
- 👥 Role-based Access Control (Admin, Job Seeker, Employer, Trainer)
- 📄 Job Posting & Application Management
- 🎓 Course Management & Enrollment
- 📊 Admin Dashboard API Support
- 📁 File Upload (e.g., Resumes, Profile Images)

## 🧪 API Modules

- **Auth API** – Register/Login/Token
- **Job API** – Post/Update/Delete/Apply for Jobs
- **User API** – Manage User Profiles
- **Course API** – Trainer uploads, seekers enroll
- **Admin API** – Platform user and content control

## ⚙️ Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL Server (or any relational DB)
- Postman (for testing APIs)

### Setup Instructions

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/Job-skill-web-backend.git

# 2. Navigate into the project directory
cd Job-skill-web-backend

# 3. Configure your database in `application.properties`

# 4. Build and run the project
mvn spring-boot:run


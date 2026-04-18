# 📦 Stockify – Inventory Management System

A full-stack Inventory Management System built using **React (frontend)** and **Spring Boot (backend)** with **PostgreSQL** as the database. The system helps manage inventory, warehouses, and stock operations efficiently with secure authentication and real-time updates.

---

## 🚀 Features

* 🔐 JWT-based Authentication & Authorization
* 📧 Email Verification System
* 📦 Product & Inventory Management
* 🏬 Warehouse-wise Stock Tracking
* 📊 Excel Upload & Download Integration
* 🏷️ Barcode Generation & Download
* 💬 Comment System with @mentions
* 🔄 Real-time Stock Updates

---

## 🛠️ Tech Stack

### Frontend

* React.js
* HTML, CSS, JavaScript

### Backend

* Spring Boot
* Java
* REST APIs

### Database

* PostgreSQL

---

## 📂 Project Structure

```
Stockify/
│
├── stockify-Frontend/   # React frontend
├── stockify-Backend/    # Spring Boot backend
├── database/            # PostgreSQL dump
├── screenshots/         # UI images
├── .gitignore
└── README.md
```

---

## ⚙️ Installation & Setup

### 1️⃣ Clone the Repository

```
git clone https://github.com/Poorvahi/Stockify.git
cd Stockify
```

---

### 2️⃣ Backend Setup (Spring Boot)

```
cd stockify-Backend
mvn clean install
mvn spring-boot:run
```

---

### 3️⃣ Frontend Setup (React)

```
cd stockify-Frontend
npm install
npm start
```

---

## 🔑 Environment Variables

### Backend (`application.properties`)

```
jwt.secret=${JWT_SECRET}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

---

### Frontend (`.env`)

```
REACT_APP_API_URL=http://localhost:8080
```

---

## 🗄️ Database Setup (PostgreSQL)

### 1. Create database

```
CREATE DATABASE ims;
```

### 2. Import database

```
psql -U postgres -d ims -f database/inventory_db.sql
```

---

## 📸 Screenshots

(Add images in `screenshots/` folder)

```
screenshots/login.png
screenshots/dashboard.png
screenshots/inventory.png
```

---

## 📌 Future Enhancements

* Role-based access control (Admin/User)
* Dashboard analytics
* Cloud deployment (AWS / Render)
* Mobile responsiveness

---

## 👩‍💻 Author

**Poorvahi**
Full Stack Developer

---

## ⭐ Resume Highlight

Developed a full-stack Inventory Management System using React and Spring Boot with JWT authentication, PostgreSQL integration, warehouse tracking, and Excel/barcode features.

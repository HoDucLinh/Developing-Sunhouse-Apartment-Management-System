# 🏢 Sunhouse Apartment Management System
A full-stack web system that supports residents and administrators in managing apartment operations, amenities, fees, surveys, packages, and real-time communication.

# 📌 Overview
The Sunhouse Apartment Management System is a comprehensive platform designed to streamline communication and operations between residents and the apartment management board.
The system includes two main portals:

Client Portal (Residents) – Built with ReactJS, connected to Firebase, VNPAY, and backend APIs via Axios.

Admin Portal (Management Board) – Built with Spring Boot + Thymeleaf, communicating with Firebase, VNPAY, Chart.js, and MySQL through Hibernate.

The architecture is illustrated below:

# 🎯 Main Features

👨‍💼 1. Account & User Management

- The management board creates new resident accounts.

- Login system with roles: Admin, BOD , and Residents.

- Residents can update their personal profiles.

- Admins can add, edit, search, lock, or unlock resident accounts in cases of transfer or leaving the apartment.

🏠 2. Room Management
  
- Admin and BOD can view room details
  
- Residents can request a room viewing appointment, which is reviewed and confirmed by administrators.

- Admins manage, edit, update all rooms

- Change room head or responsible resident

🏊 3. Utilities Registration (Pool, Gym, etc.)

- Residents can register for available utilities.

- After registering, residents must complete payment. The management board verifies and approves utility usage.

💳 4. Fee Payment System

- Residents can pay fees through two methods: Cash or Online payment (VNPAY) integrated into the system, then upload transfer receipt screenshots.

- Management board verifies and updates payment statuses.

🧾 5. Invoice & Payment History

Residents can view and export their invoices directly from the system.

Provides transparency and easy financial tracking.

🚗 6. Access card

Residents can register relatives or guests who require to BOD create the new card.

📦 7. Smart Locker / Package Management

- Each resident has a personal digital locker.

- When packages arrive, the management board receives them on behalf of residents.

- The system notifies residents via email.Residents check their locker and mark packages as received.

- Admin updates package status accordingly.

📝 8. Feedback / Complaint System

- Residents can submit feedback.

- Management board reviews and handles feedback.

📊 9. Survey System

- The management board can create surveys, which have 2 type : short answer or multiple choice.

- Residents participate to provide collective feedback.

📈 10. Admin Dashboard & Reports

Admins can view statistics about resident and revenue by month, quater, year.

💬 11. Real-time Chat

A real-time chat.

# 🏗 System Architecture
* 🔹 Frontend (Residents Portal – ReactJS)

ReactJS

Axios

Firebase Authentication & Realtime Database

VNPAY integration

Email notifications

Consume REST APIs from Spring Boot

* 🔹 Backend (Admin Portal – Spring Boot)

Spring Boot REST APIs

JWT

Thymeleaf for admin UI

Hibernate with MySQL

Firebase services

VNPAY payment integration

Chart.js for statistics visualization

🔹 Database Layer

MySQL for core data

Firebase for:

Authentication

Real-time notifications

Chat messages

📦 Technologies Used
Frontend

ReactJS

Firebase

Axios

VNPAY SDK

HTML/CSS/JS

Backend

Spring Boot

Hibernate

Thymeleaf

MySQL

Chart.js

Firebase Admin SDK

# 🚀 How to Run the Project
***
Frontend (ReactJS)
```bash
npm install
npm start
```

Backend (Spring Boot)
1. Create file .env.properties
2. Run project
```bash
mvn clean install
mvn spring-boot:run
```

# 👥 Roles
Role	Permissions
Resident	View rooms, register utilities, pay fees, view invoices, manage lockers, send feedback, join surveys, chat
Management Staff	Approve registrations, manage rooms, handle packages, process payments
Administrator	All permissions + user management + statistical reports

# 📨 Contact & Support

If you need help with deployment, architecture, or database design, feel free to ask!
Email : hoduclinh080204@gmail.com



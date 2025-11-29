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

- Residents can view and export their invoices directly from the system.

- Provides transparency and easy financial tracking.

🚗 6. Access card

- Residents can register relatives or guests who require to BOD create the new card.

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

- Admins can view statistics about resident and revenue by month, quater, year.

💬 11. Real-time Chat

- A real-time chat.

# 🏗 System Architecture

![Architecture] (https://res.cloudinary.com/dzwsdpjgi/image/upload/v1764426752/Ki%E1%BA%BFn_tr%C3%BAc_h%E1%BB%87_th%E1%BB%91ng_bl12am.png)

- Front End : ReactJS with Axios, Firebase realtime, VNPay
- Back End : Spring boot with session and JWT, thymeleaf, hibernate, firebase, VNPay, ChartJS
- REST-API

# Database Schema

![ERD] (https://res.cloudinary.com/dzwsdpjgi/image/upload/v1764428324/ERD_vn4oks.png)

# 📁 Project Structure

```
Developing-Sunhouse-Apartment-Management-System/
│
├── Sunhouse_Apartment/
│   ├── src/main/java/linh/sunhouse_apartment/
|       ├── auth/
|       ├── configs/
|       ├── controllers/
|       ├── dtos/
|       ├── entity/
|       ├── exceptions/
|       ├── repositories/
|       ├── services/
│   ├── resources/
│   ├── pom.xml
│   └── ...
└── sunhouse-apartment-fe/
|   ├── src/
|       ├── assets/
|       ├── components/
|       ├── configs/
|       ├── contexts/
|       ├── images/
|       ├── pages/
|       ├── styles/
|       ├── App.js
|       ├── index.js
|   ├── public/
|   ├── .env
|   └── package.json

```

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

# 📨 Contact & Support

If you need help with deployment, architecture, or database design, feel free to ask!

Email : hoduclinh080204@gmail.com




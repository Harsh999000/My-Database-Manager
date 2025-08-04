# 📘 Project API Tracker

This document tracks all REST API endpoints in the My-Database-Manager project, along with their HTTP methods, role access, description, and current implementation status.

---

## ✅ Authentication APIs

| Endpoint        | Method | Role Access   | Description                          | Status          |
|-----------------|--------|---------------|--------------------------------------|-----------------|
| `/auth/login`   | POST   | All roles     | Login using JWT                      |    Completed    |
| `/auth/logout`  | POST   | All roles     | Logout session and update audit log  |    Completed    |
| `/auth/signup`  | POST   | Anyone        | Register a new user with audit log   |    Completed    |

---

## 👥 User Management APIs

| Endpoint                  | Method | Role Access        | Description                        | Status         |
|---------------------------|--------|---------------------|-----------------------------------|----------------|
| `/api/get-user`           | GET    | root, super_admin   | List users with filters           |   Pending      |
| `/api/create-user`        | POST   | root, super_admin   | Create a new user                 |   Pending      |
| `/api/edit-user/{id}`     | PUT    | root, super_admin   | Edit user role/status             |   Pending      |
| `/api/delete-user/{id}`   | DELETE | root, super_admin   | Permanently delete a user         |   Pending      |

---

## 🗃️ DB/Table Viewer APIs

| Endpoint                          | Method | Role Access         | Description                                | Status          |
|-----------------------------------|--------|---------------------|--------------------------------------------|-----------------|
| `/api/get-admins`                 | GET    | root, super_admin   | List all admin users                       |    Pending      |
| `/api/get-databases`              | GET    | root, super_admin   | Get DBs by admin                           |    Pending      |
| `/api/get-tables`                 | GET    | root, super_admin   | Get tables in selected DB                  |    Pending      |
| `/api/get-table-preview`          | POST   | all roles           | Preview table content (first rows)         |    Pending      |

---

## 🔐 Table Access APIs

| Endpoint                             | Method | Role Access         | Description                               | Status          |
|--------------------------------------|--------|---------------------|-------------------------------------------|-----------------|
| `/api/get-user-access`               | GET    | root, super_admin   | Get all DB/table access for a user        |    Pending      |
| `/api/get-database-access`           | GET    | root, super_admin   | Get users with access to DB/table         |    Pending      |

---

## 📝 Audit Log APIs

| Endpoint                    | Method | Role Access         | Description                          | Status          |
|-----------------------------|--------|---------------------|--------------------------------------|-----------------|
| `/api/get-login-logs`       | GET    | root, super_admin   | View login attempts                  |    Pending      |
| `/api/get-signup-logs`      | GET    | root, super_admin   | View signup attempts                 |    Pending      |
| `/api/get-session-logs`     | GET    | root, super_admin   | View login → logout sessions         |    Pending      |
| `/api/filter-audit-logs`    | POST   | root, super_admin   | Filter logs by user/date             |    Pending      |

---

## 📊 Analytics APIs

| Endpoint                             | Method | Role Access         | Description                                   | Status          |
|--------------------------------------|--------|---------------------|-----------------------------------------------|-----------------|
| `/api/get-session-hours`             | GET    | root, super_admin   | Total session time per user                   |    Pending      |
| `/api/get-login-logout-counts`       | GET    | root, super_admin   | Daily/weekly/monthly login/logout counts      |    Pending      |
| `/api/get-user-activity-trends`      | GET    | root, super_admin   | Users created/deleted trends                  |    Pending      |
| `/api/get-db-table-trends`           | GET    | root, super_admin   | DBs/tables created/deleted over time          |    Pending      |
| `/api/get-api-usage`                 | GET    | root                | API usage analytics (planned feature)         |    Pending      |

---

🟢 ` Completed` — Feature fully built and tested  
🕒 ` Pending` — Implementation planned and in progress

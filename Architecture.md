# Architecture Document

**Project Title:** Multi-Role MariaDB Database Management System  
**Created by:** Harsh  
**Date:** 31-05-2025

---

## Objective

This system provides a secure, web-based interface for managing MariaDB databases, users, tables, and data, with **role-based dashboards** (Root, Admin, Executive-Editor, Executive-Viewer) and analytics views.

---

## Key Components

| Component             | Purpose                                                                 |
|-----------------------|-------------------------------------------------------------------------|
| **MariaDB Server**    | Stores data, manages users, databases, tables, and rows.                |
| **Java Backend**      | RESTful APIs for CRUD operations, role-based authentication, analytics. |
| **Frontend (Web UI)** | HTML/CSS/JS dashboards for each role. Dynamic search & dropdowns.       |
| **ngrok Tunnel**      | Securely expose the local web app to the internet for remote access.    |

---

## System Roles

| Role                  | Capabilities                                                                                         |
|-----------------------|------------------------------------------------------------------------------------------------------|
| **Root**              | Full access to all databases, users, tables, and data. Manages Admins & Executives.                  |
| **Admin**             | Access to own users (Executives) & databases. Cannot see or edit Root-level data or other admin data.|
| **Executive-Editor**  | Add, edit, delete tables and rows in assigned databases.                                             |
| **Executive-Viewer**  | View-only access to assigned databases & tables.                                                     |

---

## High-Level Architecture

The architecture consists of:

- **Web Browser (Frontend)**: Four dashboards (Root, Admin, Editor, Viewer) communicating with Java backend APIs.
- **Java Backend**: Manages authentication, authorization, and all database-related actions through MariaDB.
- **MariaDB Server**: Stores all data, including authentication credentials.
- **ngrok Tunnel**: Provides secure public access without exposing the home network.

---

## Data Flow

1. **User Authentication**:  
   - Passwords validated against MariaDB's native `mysql.user` table.
   - Java backend handles sessions & role-based access.

2. **Frontend Requests**:  
   - Dashboards (Root, Admin, Editor, Viewer) send REST API calls to the Java backend.
   - APIs perform role-based validation.

3. **Backend Processing**:  
   - Controllers delegate to services for core logic (CRUD - Create + Read + Update + Delete, ownership transfers, etc.).  
   - Services communicate with MariaDB using JDBC.  
   - Responses returned as JSON for frontend rendering.

4. **Analytics Data**:  
   - Root & Admin dashboards load analytics (charts, graphs).  
   - Backend aggregates data by role & date.

---

## Security Model

**Authentication**: Using MariaDB’s native user credentials.  
**Role-based access control**:  
- Root → full access  
- Admin → scoped to owned users and databases  
- Executive → scoped to admin-assigned databases and tables

**Secure ngrok tunnel** for internet access, avoiding direct exposure of home network.

---

## Deployment

- **Local Server** (no GUI) running Java backend and serving static HTML/CSS/JS frontend.
- **ngrok tunnel** provides secure external access for demonstration and testing.

---

## Key Features

- Login & role-based dashboards  
- CRUD operations (users, databases, tables, data)  
- Export data (role-based)  
- Analytics views with graphs & charts  
- Dynamic search and dropdowns  
- Modular Java backend with separate controllers for each role  
- Secure exposure using ngrok

---

## Future Enhancements

- Enhance analytics with detailed user activity logs.

---

## Next Steps

- Finalize this architecture documentation.  
- Move to the **System Design Document** (API details, data models, UI wireframes).  
- Start coding each module (begin with authentication).

---

**End of Architecture Document**  
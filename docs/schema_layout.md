
# 🗄️ Database & Table Schema Map – My Database Manager

This document outlines the finalized physical schema for all core components of the multi-tenant, access-controlled database manager project.

---

## 🔐 Authentication & User Identity

### Database: `dbmanager_user_db`

| Table          | Description                          |
|----------------|--------------------------------------|
| `user_details` | Stores all non-sensitive user metadata (username, role, status, timestamps) |

---

### Database: `dbmanager_user_secret_db`

| Table         | Description                        |
|---------------|------------------------------------|
| `user_secret` | Stores BCrypt-hashed passwords only |

---

## 🧠 Access & Mapping Layer

### Database: `dbmanager_access_db`

| Table               | Description                                      |
|---------------------|--------------------------------------------------|
| `virtual_db_map`    | Maps virtual DBs (owned by user)                 |
| `virtual_table_map` | Maps virtual tables inside a user's virtual DB   |
| `table_access_map`  | Grants table-level access to other users (executive role) |

---

## 📦 Physical User Tables (Actual Data)

### Database: `dbmanager_user_data`

- Contains all physical user tables using this format:
  ```
  <username>_<virtualDb>_<tableName>
  ```
- Examples:
  - `harsh_analytics_sales`
  - `neha_chat_messages`

---

## 🧾 Audit Logs (Private, Not in GitHub)

### Database: `dbmanager_audit_db`

| Table               | Description                                                     |
|---------------------|-----------------------------------------------------------------|
| `login_audit_log`   | Records all login attempts (success/failure, reason, IP, token) |
| `signup_audit_log`  | Records all signup attempts (status, reason, IP)                |
| `session_audit_log` | Tracks login → logout sessions with duration and logout reason  |

⚠️ **Note**: This database is **never uploaded to GitHub**. Only used for root dashboards and internal audits.

---

## 🛡️ Access Control Rules

| Role        | Access Scope                                                  |
|-------------|---------------------------------------------------------------|
| `root`      | Full access to everything (all users, all data)               |
| `super_admin` | All users and virtual DBs, except passwords                 |
| `admin`     | Only virtual DBs & tables they own                            |
| `executive` | Can only access tables assigned via `table_access_map`        |

---

## 📄 Notes

- Soft deletes use: `status`, `deleted_by`
- Audit timestamps use: `created_at`, `last_updated_at`
- No passwords or logs are committed to version control

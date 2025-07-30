# 🔐 table_access_map – Table Access Control Map

This document defines the structure and purpose of the `table_access_map` table, which governs fine-grained table-level access control in the shared schema multi-tenant architecture.

---

## 🧠 Purpose

In this system, tables are created inside a shared physical database (`dbmanager_user_data`) and tracked in `virtual_table_map`.

By default, only the table **owner** has access.  
This table (`table_access_map`) allows **non-owner users** to be granted access to specific virtual tables with specific permission scopes.

---

## 🔍 Scope of Use

- Only needed for **executive** users
- Used to control access to user-created tables across different users
- Other roles (`root`, `super_admin`, `admin`) do **not** use this table for access checks

---

## 🗃️ Table Structure: `table_access_map`

| Column Name         | Data Type     | Description                                                                |
|---------------------|---------------|----------------------------------------------------------------------------|
| `id`                | INT           | Auto-increment primary key                                                 |
| `grantee`           | VARCHAR(20)   | Username receiving access to the table (non-owner)                         |
| `owner`             | VARCHAR(20)   | Username who owns the table                                                |
| `virtual_db_name`   | VARCHAR(200)  | Virtual database name                                                      |
| `virtual_table_name`| VARCHAR(300)  | Virtual table name                                                         |
| `access_scope`      | VARCHAR(20)   | Type of access: `read`, `write`, or `read_write`                           |
| `created_at`        | DATETIME      | Timestamp when access was granted                                          |
| `created_by`        | VARCHAR(20)   | Username who granted the access                                            |
| `status`            | VARCHAR(20)   | `active`, `revoked`, etc.                                                  |

---

## 🔐 Constraints

- `(grantee, owner, virtual_db_name, virtual_table_name)` must be **UNIQUE**
- Foreign Keys:
  - `grantee` → `user_details.username`
  - `owner` → `user_details.username`
  - `(owner, virtual_db_name, virtual_table_name)` → `virtual_table_map`

---

## 🧮 Access Enforcement Logic

| Role         | Access Decision                                |
|--------------|------------------------------------------------|
| `root`       | ✅ Full access to all tables                   |
| `super_admin`| ✅ Full access to **user-created** tables only |
| `admin`      | ✅ Access to **tables they created**           |
| `executive`  | 🔍 Access based on this `table_access_map`     |

---

## 📦 Sample Entry

| id | grantee | owner | virtual_db_name | virtual_table_name   | access_scope  | created_by  | status  |
|----|---------|-------|------------------|---------------------|---------------|-------------|---------|
| 1  | neha    | harsh | analytics        | sales               | read_write    | harsh       | active  |

---

## 🧠 Notes

- Table ownership is stored in `virtual_table_map`
- This table does **not** include owner entries — only grants to other users
- Future extension may include: expiry dates, access logs, revocation reason, etc.

---

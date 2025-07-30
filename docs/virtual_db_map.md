# 🗃️ virtual_db_map – Virtual Database Mapping Table

This document defines the structure and purpose of the `virtual_db_map` table, which stores logical mappings of user-created virtual databases inside the shared physical database `dbmanager_user_data`.

---

## 🧠 Purpose

In a shared schema, multi-tenant architecture, users don't get individual physical databases. Instead, we simulate virtual databases per user using this table. This allows us to:

- Provide namespace isolation.
- Track ownership of virtual DBs.
- Abstract physical structure for clean UI/UX.
- Link to individual user-created tables (via `virtual_table_map`).

---

## 🗃️ Table Structure: `virtual_db_map`

| Column Name       | Data Type      | Description                                                                |
|-------------------|----------------|----------------------------------------------------------------------------|
| `id`              | INT            | Auto-increment primary key                                                 |
| `virtual_db_name` | VARCHAR(200)   | Name of the virtual DB (unique per user, e.g., `analytics`)                |
| `username`        | VARCHAR(20)    | Owner of this virtual DB (from `user_details.username`)                    |
| `created_at`      | DATETIME       | Timestamp when the virtual DB was created                                  |
| `last_updated_at` | DATETIME       | Timestamp of the last update to this entry                                 |
| `created_by`      | VARCHAR(20)    | Who created this entry (typically same as `username`)                      |
| `last_updated_by` | VARCHAR(20)    | Who last updated this entry                                                |
| `deleted_by`      | VARCHAR(20)    | If soft deleted, who deleted it (default: `'-'`)                           |
| `status`          | VARCHAR(20)    | Current status – `active`, `deleted`, etc.                                 |

---

## 🔐 Constraints

- `(username, virtual_db_name)` must be **unique** — no duplicate virtual DBs for the same user.
- Soft delete is handled using `status` and `deleted_by`.
- `username` is a foreign key referencing `user_details.username`.

---

## 🧮 Physical Table Naming Convention

Any table created under a virtual DB is stored in `dbmanager_user_data` using this format:

<username><virtual_db_name><table_name>

For example:  
A user `harsh` creates a virtual DB `analytics` and a table `sales` →  
Actual physical table name will be:

harsh_analytics_sales

---

## 🔗 Relationships

- `username` → foreign key to `user_details.username`
- `(username, virtual_db_name)` → foreign key reference in `virtual_table_map`

---

## 📦 Sample Entry

| id | virtual_db_name  | username | created_at          | created_by | status  |
|----|------------------|----------|---------------------|------------|---------|
| 1  | analytics        | harsh    | 2025-07-30 10:15:00 | harsh      | active  |

---


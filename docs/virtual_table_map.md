# 🗂️ virtual_table_map – Virtual Table Mapping Table

This document defines the structure and purpose of the `virtual_table_map` table, which tracks each table created by users within their virtual databases. All such tables are physically stored in the shared database `dbmanager_user_data`.

---

## 🧠 Purpose

In a shared schema multi-tenant setup, every table created by a user is stored in a shared DB with a uniquely derived name. This mapping table allows:

- Tracking logical table names vs physical names
- Linking tables to their virtual database and owner
- Enabling structured UI views and access controls
- Mapping column metadata (via future `column_map` if required)

---

## 🗃️ Table Structure: `virtual_table_map`

| Column Name        | Data Type      | Description                                                                |
|--------------------|----------------|----------------------------------------------------------------------------|
| `id`               | INT            | Auto-increment primary key                                                 |
| `virtual_table_name` | VARCHAR(300) | Logical name of the table (e.g., `sales`)                                  |
| `virtual_db_name`  | VARCHAR(200)   | Virtual DB under which this table is created                               |
| `username`         | VARCHAR(20)    | Owner of the table and the virtual DB                                      |
| `created_at`       | DATETIME       | Timestamp when the table was created                                       |
| `last_updated_at`  | DATETIME       | Timestamp of the last update                                               |
| `created_by`       | VARCHAR(20)    | Who created this entry                                                     |
| `last_updated_by`  | VARCHAR(20)    | Who last updated this entry                                                |
| `deleted_by`       | VARCHAR(20)    | If soft deleted, who deleted it (default: `'-'`)                           |
| `status`           | VARCHAR(20)    | `active`, `deleted`, etc.                                                  |

---

## 🔐 Constraints

- `(username, virtual_db_name, virtual_table_name)` must be **unique**
- Soft delete is handled using `status` and `deleted_by`
- Foreign key references:
  - `username` → `user_details.username`
  - `(username, virtual_db_name)` → `virtual_db_map(username, virtual_db_name)`

---

## 🧮 Physical Table Naming Convention

The actual table is stored physically in `dbmanager_user_data` as:

<username><virtual_db_name><virtual_table_name>

Example:  
If user `harsh` creates a table `sales` in virtual DB `analytics`,  
the actual table is named:

harsh_analytics_sales

---

## 🔗 Relationships

- `username` → foreign key to `user_details.username`
- `(username, virtual_db_name)` → foreign key to `virtual_db_map`
- Maps to physical table name using naming convention only (not stored directly)

---

## 📦 Sample Entry

| id | virtual_table_name  | virtual_db_name  | username | created_by | status  |
|----|---------------------|------------------|----------|------------|---------|
| 1  | sales               | analytics        | harsh    | harsh      | active  |

---


# 📘 Table: user_details

**Database**: `dbmanager_user_db`  
**Purpose**: Stores non-sensitive metadata of users in the system such as username, role, status, and audit tracking fields.

---

## 🧱 Table Schema

| Column Name        | Data Type                           | Description                                                                 |
|--------------------|-------------------------------------|-----------------------------------------------------------------------------|
| `id`               | INT, PRIMARY KEY, AUTO_INCREMENT    | Unique user ID (auto-incrementing from 1)                                   |
| `username`         | VARCHAR(20), UNIQUE, NOT NULL       | Unique user login name                                                      |
| `status`           | VARCHAR(20), NOT NULL               | Must be either `'active'` or `'inactive'`                                   |
| `role`             | VARCHAR(20), NOT NULL               | Must be one of `'root'`, `'super_admin'`, `'admin'`, `'executive'`          |
| `created_at`       | DATETIME, DEFAULT CURRENT_TIMESTAMP | Timestamp at creation                                                       |
| `last_updated_at`  | DATETIME, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Auto-updated on every row update                |
| `created_by`       | VARCHAR(20), NOT NULL               | Username of the creator of this user                                        |
| `last_updated_by`  | VARCHAR(20), NOT NULL               | Username of the last updater                                                |
| `deleted_by`       | VARCHAR(20), DEFAULT '-'            | Username who deleted/inactivated the user (if applicable)                   |

---

## 🔐 Constraints

- **Primary Key**: `id`
- **Unique Constraint**: `username`
- **Check Constraints**:
  - `status` IN (`'active'`, `'inactive'`)
  - `role` IN (`'root'`, `'super_admin'`, `'admin'`, `'executive'`)
- **Auto-Timestamps**:
  - `created_at` is fixed once inserted
  - `last_updated_at` updates automatically on every change

---

## 🧠 Design Rationale

- Keeping credentials (e.g., password hash) in a **separate database** (`dbmanager_user_secret_db`) ensures super_admins cannot access sensitive info.
- Role and status are string-checked with backend and DB-side constraints.
- The `deleted_by` field helps track who disabled/deactivated users without permanent deletion.

---

## ✅ Example Insert

```sql
INSERT INTO user_details (
  username, status, role, created_by, last_updated_by
)
VALUES (
  'admin1', 'active', 'admin', 'root', 'root'
);

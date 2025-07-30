# 📘 Table: user_secret

**Database**: `dbmanager_user_secret_db`  
**Purpose**: Securely stores bcrypt-hashed passwords for system users. Separated from user metadata to prevent unauthorized access.

---

## 🧱 Table Schema

| Column Name       | Data Type        | Description                                                           |
|-------------------|------------------|-----------------------------------------------------------------------|
| `username`        | VARCHAR(20)      | Primary Key — must correspond to a user in `user_details`             |
| `password_hash`   | VARCHAR(500)     | bcrypt-hashed password string                                         |
| `created_at`      | DATETIME         | Timestamp of password creation                                        |
| `last_updated_at` | DATETIME         | Auto-updated whenever password is changed                             |

---

## 🔐 Constraints

- **Primary Key**: `username`
- **NOT NULL**: `password_hash`
- **Foreign Key** *(manual, enforced in backend)*: `username` must exist in `user_details`
- **Timestamps**:
  - `created_at` → Default to `CURRENT_TIMESTAMP`
  - `last_updated_at` → Auto-updated on change via `ON UPDATE CURRENT_TIMESTAMP`

---

## 🔄 Hashing Standard

- Uses **bcrypt** hashing algorithm with built-in random salt.
- Hashes are **non-reversible** and vary on every hash generation.

---

## ⚙️ Handled by Backend

- Passwords are hashed in backend code before storage (e.g., using `BCryptPasswordEncoder` in Spring).
- No plain password ever stored or transmitted to the DB.

---

## ✅ Example Insert (Backend Controlled)

```sql
INSERT INTO user_secret (
username, password_hash
)
VALUES (
'harsh123', '$2a$10$xyz...hashedpassword...abc'
);

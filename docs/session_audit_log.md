# 📘 Table: session\_audit\_log

**Database**: `dbmanager_audit_db`
**Purpose**: Tracks login → logout sessions for each user, storing session durations, token details, and logout reasons. This table helps root users monitor session history and detect unusual behavior.

---

## 👱️ Table Schema

| Column Name     | Data Type    | Description                                               |
| --------------- | ------------ | --------------------------------------------------------- |
| `id`            | INT          | Auto-increment primary key                                |
| `username`      | VARCHAR(20)  | User who logged in                                        |
| `token_hash`    | VARCHAR(300) | SHA256-hashed version of JWT used                         |
| `login_time`    | DATETIME     | Timestamp when session started (login)                    |
| `logout_time`   | DATETIME     | Timestamp when session ended (logout); nullable if active |
| `logout_reason` | VARCHAR(100) | Reason for logout: `user`, `expired`, `forced`, etc.      |
| `ip_address`    | VARCHAR(50)  | IP address from which session originated                  |
| `user_agent`    | VARCHAR(200) | Browser or client identifier                              |
| `created_by`    | VARCHAR(20)  | Always set to `system` or the `username` itself           |

---

## 🔐 Constraints

* **Primary Key**: `id`
* **Not Null**: All fields except `logout_time` (nullable if session is still active)
* **Hashing**: `token_hash` stores a SHA256 hash, not the raw JWT
* **Created By**: Helps trace automated vs user-created entries

---

## 🔎 Use Cases

| Scenario                    | Outcome                                                  |
| --------------------------- | -------------------------------------------------------- |
| Normal user logout          | `logout_time` set, `logout_reason` = `user`              |
| Token expired               | `logout_time` set by system, `logout_reason` = `expired` |
| Manual force logout by root | `logout_time` set by system, `logout_reason` = `forced`  |
| Still logged in             | `logout_time` is `NULL`                                  |

---

## ⚠️ Notes

* This table is **not uploaded to GitHub** (private to server)
* Useful for root dashboards, session analytics, and audit trails
* Timestamp format should align with server timezone or use UTC consistently
* Logout logging is done either via `/auth/logout` API or background job (future)

---

## 📦 Example Entry

| id | username | token\_hash | login\_time         | logout\_time        | logout\_reason | ip\_address   | user\_agent | created\_by |
| -- | -------- | ----------- | ------------------- | ------------------- | -------------- | ------------- | ----------- | ----------- |
| 1  | harsh    | abc123...   | 2025-08-03 09:30:00 | 2025-08-03 10:10:00 | user           | 192.168.0.194 | Mozilla/5.0 | harsh       |

# 📘 Table: api_audit_log

**Database**: `dbmanager_audit_db`  
**Purpose**: Tracks every API call made to the server for audit and analytics purposes. Helps identify which user accessed which endpoint, using what method, device, and origin.

---

## 🧱 Table Schema

| Column Name    | Data Type     | Description                                                                 |
|----------------|---------------|-----------------------------------------------------------------------------|
| `id`           | INT           | Auto-increment primary key                                                  |
| `username`     | VARCHAR(20)   | Logical app user making the request (extracted from JWT token)              |
| `endpoint`     | VARCHAR(300)  | API endpoint hit (e.g., `/api/create-user`)                                 |
| `method`       | VARCHAR(10)   | HTTP method (`GET`, `POST`, etc.)                                           |
| `ip_address`   | VARCHAR(100)  | IP address of the client making the request                                 |
| `user_agent`   | TEXT          | Client/browser agent string (e.g., Mozilla, Postman)                        |
| `source_type`  | VARCHAR(30)   | Inferred source: `web_ui`, `postman`, `curl_script`, etc.                   |
| `status_code`  | INT           | HTTP status code returned (e.g., 200, 401, 500)                             |
| `timestamp`    | DATETIME      | Time when request completed (default: `CURRENT_TIMESTAMP`)                  |
| `created_by`   | VARCHAR(20)   | Same as `username` — retained for future compatibility                      |

---

## 🔐 Constraints & Notes

- Does **not** track database-level user (`app_user`) — this is purely application-layer.
- All data is captured in the backend via interceptor and stored with JDBC.
- No foreign keys to ensure fast writes.
- `source_type` is inferred from `User-Agent` header to detect origin (e.g., Postman, browser).
- Recommended indexes: `username`, `timestamp`, `endpoint`

---

## 📦 Example Entry

| id | username | endpoint         | method | ip_address    | source_type | status_code | user_agent          | timestamp           | created_by  |
|----|----------|------------------|--------|---------------|-------------|-------------|---------------------|----------------------|------------|
| 1  | harsh    | /api/create-user | POST   | 192.168.0.101 | web_ui      | 200         | Mozilla/5.0         | 2025-08-04 10:35:21  | harsh      |

---

## 🧠 Use Cases

- Track misuse or high-frequency hits
- Identify frequent error patterns
- Attribute API activity to real users even when all DB queries go via `app_user`
- Differentiate between frontend usage vs external tools (Postman, curl)

---

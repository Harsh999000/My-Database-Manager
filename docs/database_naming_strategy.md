# 🗄️ Database Naming & Structure Strategy – My Database Manager (Updated)

This document outlines the finalized architecture, naming conventions, and mapping strategy for handling multi-user, multi-database environments using a **single shared physical MariaDB database**.

---

## 🧠 Problem Statement

When users (especially admins or super_admins) can create multiple databases and tables, we face challenges like:
- **Name collisions** (e.g., two users create `analytics_db`)
- **Excessive database creation** leading to storage and performance issues
- Difficulty enforcing access controls across multiple physical databases

---

## ✅ Final Strategy: Shared DB + Namespaced Tables (Multi-Tenant Model)

| Layer        | What user sees (UI)         | What is created (MariaDB)                                     |
|--------------|-----------------------------|---------------------------------------------------------------|
| Virtual DB   | `analytics_db`              | Metadata entry in `virtual_db_map`                            |
| Virtual Table| `messages` in `chat_db`     | Physical table: `harsh123_chat_db_messages` in `user_data_db` |

- All user-created tables live in a **single shared MariaDB database**: `user_data_db`
- Actual table names are **namespaced** with:  
  `username_virtualdbname_tablename`

---

## 📦 Example Table Naming

| Virtual DB | Virtual Table  | Owner      | Physical Table Name                 |
|------------|----------------|------------|--------------------------------------|
| `chat_db`  | `messages`     | `harsh123` | `harsh123_chat_db_messages`          |
| `chat_db`  | `messages`     | `admin99`  | `admin99_chat_db_messages`           |
| `hr_db`    | `employees`    | `admin99`  | `admin99_hr_db_employees`            |

This allows:
- Different users to use **same virtual DB and table names**
- Simple reverse-mapping to user and logical DB
- Avoids DB-level resource explosion in MariaDB

---

## 📁 Core Mapping Tables

### 1. `virtual_db_map`

| Column         | Description                              |
|----------------|------------------------------------------|
| `id`           | Unique ID                                |
| `db_name`      | Virtual DB name                          |
| `user_id`      | Owner of the DB (FK → `user_details.id`) |
| `created_at`   | Timestamp of creation                    |

### 2. `virtual_table_map`

| Column         | Description                            |
|----------------|----------------------------------------|
| `id`           | Unique ID                              |
| `table_name`   | Virtual table name                     |
| `db_id`        | FK → `virtual_db_map.id`               |
| `user_id`      | Owner of the table                     |
| `physical_name`| Actual table name in MariaDB           |

---

## 🔐 Access Control

- Only users who **own** the virtual DB or are granted access via `table_access_map` can access data
- Backend enforces all access rules using:
  - `user_id`
  - `db_id`
  - `physical_name` lookup via mapping tables

---

## 🎯 Enforced Rules

| Rule                                                   | Enforced Where               |
|--------------------------------------------------------|------------------------------|
| ✅ All user tables stored in one physical DB           | In `user_data_db`            |
| ✅ Table names are globally unique by namespacing      | `username_db_table` format   |
| ✅ Virtual DB names can repeat across users            | Handled in `virtual_db_map`  |
| ✅ All access controlled by metadata mappings          | App-layer enforcement        |
| ✅ Super_admins see all metadata, not passwords        | Design-level enforced        |

---

## 🧰 Real-World Alignment

This design aligns with the architecture used by:
- Multi-tenant SaaS platforms (e.g., Stripe, Notion)
- Workspace-based database providers (e.g., Firebase, Supabase)
- Shared-schema services with logical DB boundaries

It enables:
- 🔄 Easy scaling
- 🔐 Strict access control
- 💽 Storage efficiency
- 📊 Fast metadata queries

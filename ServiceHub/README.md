# ServiceHub

ServiceHub is a Spring Boot service booking website. Phase 2 adds MySQL-backed user registration, BCrypt password hashing, and session authentication.

## Run locally

Requirements: Java 17+, Maven 3.9+, and MySQL 8+.

## Configure MySQL

Create the database once:

```sql
CREATE DATABASE servicehub_db;
```

The application creates the `users` table through JPA on first startup. The local development connection uses `root` with a blank password because the included local setup initializes MySQL that way; override it with `DB_USERNAME`, `DB_PASSWORD`, or `DB_URL` environment variables. Database credentials stay in server configuration and are never included in frontend code.

The default development admin is created if it does not already exist:

- Email: `admin@servicehub.local`
- Password: `Admin@12345`

Override these with `ADMIN_EMAIL`, `ADMIN_PASSWORD`, `ADMIN_NAME`, and `ADMIN_PHONE` environment variables before first startup.

```bash
mvn spring-boot:run
```

Open [http://localhost:8081](http://localhost:8081) in a browser. Registration and login are database-backed; booking persistence is not implemented yet.

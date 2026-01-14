# Setup Instructions

## Prerequisites
- Java 21
- Gradle
- MariaDB installed

> This project uses MariaDB with the following default credentials (as required for marking):
> - Username: `root`
> - Password: `comsc`
> - Database name: `dsfw_team_proj`

---

## 1) Start MariaDB

### Linux (Debian/Kali/Ubuntu)
```bash
sudo systemctl start mariadb
sudo systemctl enable mariadb
sudo systemctl status mariadb --no-pager
````

(Optional) Verify MariaDB is listening on port 3306:

```bash
ss -lntp | grep 3306 || sudo ss -lntp | grep 3306
```

### Windows

Start the **MariaDB** service from **Services** (services.msc) or the MariaDB installer tools.

---

## 2) Ensure root password is set to `comsc`

### Log in to MariaDB (password login)

Try:

```bash
mariadb -u root -p
```

When prompted, enter:

```text
comsc
```

### If password login fails on Linux (socket authentication)

Some Linux installs configure MariaDB root login using socket authentication. Try:

```bash
sudo mariadb
```

> If you still cannot access MariaDB as root, follow your system’s MariaDB root reset procedure (or ask the project author).

---

## 3) Create the database (REQUIRED before first run)

> You only need to do this **once** (the first time you run the project, or if you delete the database).

⚠️ IMPORTANT:

- These are **SQL commands**.

- They must be run **inside the MariaDB prompt**, not in the Linux terminal.


After logging in (`mariadb -u root -p` or `sudo mariadb`), run:

```sql
CREATE DATABASE IF NOT EXISTS dsfw_team_proj;

GRANT ALL PRIVILEGES ON dsfw_team_proj.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

Exit MariaDB:

```sql
EXIT;
```

### Verify the database exists

```bash
mariadb -u root -p -e "SHOW DATABASES;"
```

Password:

```text
comsc
```

You should see `dsfw_team_proj` in the output.

### Windows note (if `mariadb` isn’t available in CMD/PowerShell)

You can usually use:

```bat
mysql -u root -p
```

Then run the same SQL commands above.

---

## 4) Configure `application.properties`

The default configuration (recommended for marking) should be:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/dsfw_team_proj
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:comsc}
```
#### (Optional) How to Create Environment Variables
1. Click on the three vertical dots next to the run/debug button on the top right.
2. Click on `Edit` under `Configuration`.
3. Copy and paste this in the `Environment Variables` input field: `DB_USERNAME=root;DB_PASSWORD=comsc`
4. Click on `Apply` then click on `OK`.
5. If you run the app now, it should use the environment variables instead of the hardcoded credentials.

Ideally, in production you would not want to use hardcoded credentials like the `root` user and its `comsc` password as it would be publically visible on version control software lie GitLab or GitHub.

---

## 5) Run the web app

### Linux/macOS

```bash
./gradlew bootRun
```

### Windows

```bat
gradlew.bat bootRun
```

Then open:

- [http://localhost:8080](http://localhost:8080)


---

## Database initialization notes

- Tables are created/updated automatically via Hibernate:

  - `spring.jpa.hibernate.ddl-auto=update`

- Initial data is inserted using:

  - `src/main/resources/data.sql`


---


## Admin Account Setup

**SECURITY UPDATE:**
The default admin account has been removed from automatic creation for security reasons.

### For Assessors/Testing:

To create an admin account manually, run the following commands in the intellij terminal under `~/digital-skill-for-wales-team-proj-cyber-security` directory:

1. **Step 1:**
```bat
mysql -u root -p
```
2. **Step 2:**
```bat
comsc
```
3. **Step 3:**
```sql
-- Connect to database
USE dsfw_team_proj;
```
4. **Step 4:**
```sql
-- Create admin account (password: SecureAdmin2024!)
INSERT INTO user_details (first_name, last_name, email, password, role)
VALUES ('Admin', 'Assessor', 'admin@cardiff.ac.uk', 
        '$2a$12$LQI.eBfTXHjOtmHvCYNcQO7YzT2ZqXGhQvz4JLMkiLMR5L6Hy5rC6',
        'ADMIN');
```

**Login Credentials:**
- Email: `admin@cardiff.ac.uk`
- Password: `SecureAdmin2024!`

---

## Troubleshooting

### “Unknown database 'dsfw_team_proj'”

The database has not been created yet. Run Step 3.

### “Connection refused”

MariaDB is not running. Run Step 1.

### “Access denied for user 'root'@'localhost'”

The root password is not `comsc`, or root authentication is configured differently on your system.

Try:

- `mariadb -u root -p` (password: `comsc`)

- On Linux, try `sudo mariadb`
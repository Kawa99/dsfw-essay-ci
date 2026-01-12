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
spring.datasource.username=root
spring.datasource.password=comsc
```

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

## Default Admin Account

- Email: [admin@test.com](mailto:admin@test.com)

- Password: Admin1234@@


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
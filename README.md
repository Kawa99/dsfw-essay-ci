# Setup Instructions

## Prerequisites
- Java 21
- MariaDB installed and running
- Gradle

## Quick Start

1. Start MariaDB service

2. Update `application.properties` under main with your database credentials:
    - spring.datasource.username=root
    - spring.datasource.password=comsc

3. Run the application:
```bash
   ./gradlew bootRun
```

Or on Windows:
```bash
   gradlew.bat bootRun
```

4. Access the application at: http://localhost:8080

## Default Admin Account
- Email: admin@test.com
- Password: Admin1234@@
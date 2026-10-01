# Spring Security Roles

A small practice project showing role-based access control with Spring Security. It has one public page and two pages restricted by role, with hard-coded users and BCrypt-hashed passwords.

## Tech stack

- Java, Spring Boot
- Spring Web
- Spring Security

## Endpoints

| Endpoint     | Access               | Description         |
|--------------|----------------------|---------------------|
| `/home`      | Public               | Home page           |
| `/employees` | Role `EMPLOYEE` only | Employee page       |
| `/admin`     | Role `ADMIN` only    | Admin page          |
| `/logout`    | Logged-in users      | Ends the session    |

Any other URL requires the user to be logged in.

## How it works

- **Authentication:** form login (browser) and HTTP Basic (Postman, curl).
- **Authorization:** `requestMatchers` rules in `EmployeeSecurity` map each URL pattern to a role. Rules are checked top to bottom, and the first match wins.
- **Passwords:** hashed with BCrypt before they are stored. No plain-text passwords.
- **Users:** two hard-coded in-memory users, for demonstration only.

| Username | Role       |
|----------|------------|
| Mauro    | `ADMIN`    |
| Elcin    | `EMPLOYEE` |

- **Logout:** invalidates the session, deletes the `JSESSIONID` cookie and returns a plain confirmation message.

## Running the project

1. Clone the repository.
2. Run the application from your IDE or with `./gradlew bootRun`.
3. Open `http://localhost:8080/home`, or request a protected URL and log in.

## Security notes

- CSRF protection is disabled in this project so the endpoints are easy to test with Postman. This is acceptable only because the app holds no real data. An application with cookie-based login and real users should keep CSRF protection enabled.
- Users and passwords are hard-coded for the exercise. A real application would load users from a database.

## What I learned

Role-based access control, BCrypt password hashing, `SecurityFilterChain` configuration, URL pattern matching (`/admin/**` vs `/admin/*`) and session-based logout.
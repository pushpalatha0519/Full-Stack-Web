# BusyBrains Ecommerce Assignment

Full-stack ecommerce demo built with:

- React + Vite frontend
- Java 17 + Spring Boot backend
- Spring Security with JWT authentication
- OAuth2/OpenID Connect ready SSO flow
- H2 in-memory database for quick local setup

## Features

- User registration and login
- Seeded demo users with RBAC
- Product dashboard similar to a lightweight ecommerce catalog
- Admin-only create, update, and delete product actions
- User-only read access to products
- Profile update and password change
- OAuth2 redirect flow for Google SSO, with extension points for Microsoft/Facebook

## Demo Users

- Admin
  - Username: `admin`
  - Password: `admin123`
- User
  - Username: `user`
  - Password: `user123`

## Project Structure

- [frontend](/c:/Users/DELL/Downloads/ecommerce/frontend) - React client
- [ecommerce](/c:/Users/DELL/Downloads/ecommerce/ecommerce) - Spring Boot API

## Backend Setup

1. Open a terminal in [ecommerce](/c:/Users/DELL/Downloads/ecommerce/ecommerce)
2. Run `.\mvnw.cmd spring-boot:run`
3. Backend starts at `http://localhost:8080`

Useful endpoints:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/products`
- `POST /api/products` admin only
- `PUT /api/products/{id}` admin only
- `DELETE /api/products/{id}` admin only
- `GET /api/profile`
- `PUT /api/profile`
- `PUT /api/profile/password`

H2 console:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: empty

## Frontend Setup

1. Open a terminal in [frontend](/c:/Users/DELL/Downloads/ecommerce/frontend)
2. Run `npm.cmd install`
3. Run `npm.cmd run dev`
4. Frontend starts at `http://localhost:5173`

## SSO Setup

The app is already wired for OAuth2 login success handling. To demo Google SSO:

1. Create OAuth credentials in Google Cloud Console
2. Add redirect URI:
   `http://localhost:8080/login/oauth2/code/google`
3. Update [application.properties](/c:/Users/DELL/Downloads/ecommerce/ecommerce/src/main/resources/application.properties) with:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,profile,email
```

The frontend button sends users to `http://localhost:8080/oauth2/authorization/google`.

## Verification

- Backend verified with `.\mvnw.cmd test`
- Frontend verified with `npm.cmd run build`

## GitHub Submission

To publish this to your private repository:

1. Run `git init`
2. Run `git add .`
3. Run `git commit -m "Complete BusyBrains ecommerce assignment"`
4. Create a private GitHub repository
5. Add the remote and push

Example:

```bash
git remote add origin <your-private-repo-url>
git branch -M main
git push -u origin main
```

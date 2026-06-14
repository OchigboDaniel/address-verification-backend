# AddressVerify — Backend

A Spring Boot REST API that verifies a user's physical presence at an address using GPS coordinates from their device.

---

## The Problem

Traditional address verification relies on self-reported data — users type in an address and it's taken at face value. AddressVerify solves this by cross-referencing a user's claimed address with their actual GPS coordinates at the time of verification, using the Google Geocoding API to resolve coordinates into a structured address.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 + Spring Boot 3 | Core framework |
| Spring Security + JWT | Authentication and authorization |
| Spring Data JPA + Hibernate | Database access and ORM |
| JPA Specifications | Dynamic filtering |
| PostgreSQL | Persistence |
| Google Geocoding API | Coordinate-to-address resolution |
| Maven | Dependency management |

---

## Features

- **JWT Authentication** — stateless token-based auth with secure login and signup
- **Role-Based Access Control** — three roles: `USER`, `MANAGER`, `ADMIN` with endpoint-level protection
- **GPS Address Verification** — resolves latitude/longitude to a formatted address via Google Geocoding API
- **Paginated Admin Endpoint** — returns verification records with pagination metadata (`totalPages`, `totalElements`, `currentPage`)
- **Dynamic Filtering** — filter records by country, state, or email using JPA Specifications; case-insensitive and partial match supported
- **CSV Export** — exports all verification records as a downloadable `.csv` file

---

## Architecture

```
Client Request
      │
      ▼
Spring Security Filter Chain
      │
      ├── JwtAuthFilter (validates Bearer token)
      │
      ▼
Controller Layer
      │
      ▼
Service Layer
      │
      ├── VerifyAddressService → Google Geocoding API
      │
      ▼
Repository Layer (JPA + Specifications)
      │
      ▼
PostgreSQL Database
```

---

## API Endpoints

### Auth
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login and receive JWT + role |

### Address
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/verify-address` | USER | Verify GPS location |
| GET | `/api/address` | ADMIN, MANAGER | Get paginated records |
| GET | `/api/address?export=true` | ADMIN, MANAGER | Download CSV export |

### Query Parameters for GET `/api/address`
| Parameter | Default | Description |
|---|---|---|
| `page` | `0` | Page number (0-indexed) |
| `size` | `10` | Records per page |
| `sortBy` | `id` | Field to sort by |
| `direction` | `asc` | Sort direction (`asc` / `desc`) |
| `country` | `""` | Filter by country (partial, case-insensitive) |
| `state` | `""` | Filter by state (partial, case-insensitive) |
| `email` | `""` | Filter by user email (partial, case-insensitive) |

---

## Getting Started

### Prerequisites
- Java 17+
- Maven
- PostgreSQL
- Google Geocoding API key

### Environment Variables

Create an `application.properties` or use environment variables:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/addressverify
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

JWT_SECRET=your_jwt_secret
GEO_CODE_URL_GOOGLE=
GEO_CODE_API_KEY_GOOGLE=your_google_api_key
DB_URL=your_db_url
DB_USERNAME=your_db_name
DB_PASSWORD=your_db_password
GEO_CODE_URL_GOOGLE=https://geocode.googleapis.com/v4/geocode/location
GEO_CODE_API_KEY_GOOGLE=your_google_api_key
JWT_EXPIRE_TIME=3600000
JWT_SECRETE_KEY=your_jwt_key
```

### Run Locally

```bash
git clone https://github.com/OchigboDaniel/address-verification-backend
cd address-verification-backend
mvn spring-boot:run
```

API will be available at `http://localhost:8080`

---

## Known Limitations & Future Improvements

| Limitation | Planned Improvement |
|---|---|
| JWT stored in localStorage on frontend | Move to httpOnly cookies to prevent XSS attacks |
| No token refresh mechanism | Implement refresh token rotation |
| Google Geocoding API has usage limits | Add caching layer to reduce redundant API calls |
| No rate limiting on auth endpoints | Add Spring Security rate limiting to prevent brute force |
| CSV export loads all records into memory | Stream large exports directly to response |

---

## Author

**Daniel** — Java Backend Developer  
[GitHub](https://github.com/OchigboDaniel) · [LinkedIn](www.linkedin.com/in/daniel-ochigbo-2a77b7229)

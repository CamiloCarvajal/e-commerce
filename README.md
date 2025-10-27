# E-commerce

Technical test for blossom.


### Key Principles

- **Domain Independence**: Domain layer has no dependencies on frameworks
- **Repository Pattern**: Interfaces in domain, implementations in infrastructure
- **Dependency Inversion**: High-level modules don't depend on low-level modules
- **Single Responsibility**: Each layer has a specific purpose

##  Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher

### Running the Application

```bash
# Clone the repository
git clone  https://github.com/CamiloCarvajal/e-commerce.git
cd ecommerce

# Run the application
.\gradlew bootRun

# Or build and run
.\gradlew build
java -jar build/libs/ecommerce-0.0.1-SNAPSHOT.jar
```

### Database Console

Access the H2 console at: `http://localhost:8080/h2-console`

For h2 user configuration go to src\main\resources\application.properties and set the properties:
- JDBC URL: `jdbc:h2:mem:blossom`
- Username: `sa`
- Password: `<set_a_password>`

##  API Documentation

### AuthController (`/api/auth`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| POST | `/api/auth/login` | Authenticate user and receive JWT token | Public |
| POST | `/api/auth/register` | Register a new user | Public |

### UserController (`/api/users`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| GET | `/api/users` | Get all users | ADMIN only |
| GET | `/api/users/{email}` | Get user by email (or current user if empty) | Authenticated |
| POST | `/api/users` | Create new user | Authenticated |
| PUT | `/api/users/{email}` | Update user by email | Authenticated |
| DELETE | `/api/users/{email}` | Delete user by email | Authenticated |
| GET | `/api/users/{email}/info` | Get user info with purchase history | Authenticated |

### ProductController (`/api/products`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| GET | `/api/products` | Get all products (supports filtering & sorting) | Authenticated |
| GET | `/api/products/{id}` | Get product by ID | Authenticated |
| POST | `/api/products` | Create a new product | Authenticated |
| PUT | `/api/products/{id}` | Update product by ID | Authenticated |
| DELETE | `/api/products/{id}` | Delete product by ID | Authenticated |

**Query parameters for filtering:**
- `name` - Filter by product name (case-insensitive)
- `minPages` / `maxPages` - Filter by number of pages
- `minCost` / `maxCost` - Filter by price range
- `author` - Filter by author (for books)
- `lineType` - Filter by line type (for notebooks)
- `sortBy` - Sort by "cost", "name", or "pages"
- `sortOrder` - "asc" or "desc"

### PurchaseController (`/api/purchases`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| GET | `/api/purchases` | Get all purchases | ADMIN only |
| GET | `/api/purchases/{id}` | Get purchase by ID | Authenticated |
| POST | `/api/purchases` | Create a new purchase (auto-calculates total) | Authenticated |
| PUT | `/api/purchases/{id}` | Update purchase status | Authenticated |
| GET | `/api/purchases/user/me` | Get current user's purchases | Authenticated |
| GET | `/api/purchases/user/me/status/{status}` | Get user's purchases filtered by status | Authenticated |

**Status values:** PENDING, PAID, CANCELLED

### PaymentController (`/api/payments`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| GET | `/api/payments` | Get all payment methods | Authenticated |
| GET | `/api/payments/{id}` | Get payment method by ID | Authenticated |
| POST | `/api/payments` | Create a new payment method | Authenticated |
| PUT | `/api/payments/{id}` | Update payment method by ID | Authenticated |
| DELETE | `/api/payments/{id}` | Delete payment method by ID | Authenticated |

### RoleController (`/api/roles`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| GET | `/api/roles` | Get all roles | Authenticated |
| GET | `/api/roles/{id}` | Get role by ID | Authenticated |
| POST | `/api/roles` | Create a new role | Authenticated |
| PUT | `/api/roles/{id}` | Update role by ID | Authenticated |
| DELETE | `/api/roles/{id}` | Delete role by ID | Authenticated |

### Authentication

All authenticated endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

##  Security

User sensitive data is encrypted using AES:
- Encrypted fields: `last_name`, `address`, `city`, `country`, `phone`
- Encryption keys configured in `application.properties`
- Automatic encryption on save
- Automatic decryption on retrieve

##  Testing

### Run All Tests
```bash
.\gradlew test
```

### Run Specific Test Suite
```bash
# Use Case tests
.\gradlew test --tests "*.usecase.*"

# Controller tests
.\gradlew test --tests "*.rest.UserControllerTest"
```

### Test Coverage
- **51 Unit Tests** - Use case layer with mocked dependencies
- **14 Integration Tests** - Controller layer with MockMvc



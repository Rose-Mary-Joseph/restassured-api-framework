# REST API Test Automation Framework

A production-grade API testing framework built with **RestAssured**, **TestNG**, and **Allure Reports**. Demonstrates CRUD testing, JSON Schema validation, authentication flows, and CI-ready configuration.

> Built as a portfolio project showcasing API automation skills.

---

## Tech Stack

| Tool | Purpose |
|------|---------|
| Java 17 | Programming language |
| RestAssured 5.4 | HTTP client for API testing |
| TestNG 7.10 | Test execution engine |
| Jackson 2.17 | JSON serialization/deserialization |
| JSON Schema Validator | Response structure validation |
| Allure 2.27 | HTML reporting with request/response logs |
| Maven | Build and dependency management |

---

## Project Architecture
```
restassured-api-framework/
├── src/
│   ├── main/java/com/api/
│   │   ├── config/       # ConfigReader (Singleton, CLI-overridable)
│   │   ├── endpoints/    # BaseApi + endpoint classes (API Page Objects)
│   │   ├── models/       # POJOs for request/response serialization
│   │   └── utils/        # Shared utilities
│   └── test/
│       ├── java/com/api/tests/   # Test classes (UserTests, AuthTests)
│       └── resources/
│           ├── config/           # config.properties
│           └── schemas/          # JSON Schema files for validation
├── testng.xml                    # Parallel execution configuration
├── pom.xml                       # Maven dependencies
└── README.md
```

---

## Key Design Decisions

### Endpoint Classes as API Page Objects
Each API resource gets its own class (`UserEndpoint`, `AuthEndpoint`) that encapsulates HTTP calls. Tests never build raw RestAssured chains — they call clean methods like `UserEndpoint.createUser(user)`. If a URL changes, one file is updated.

### POJO-Based Serialization
Request and response bodies are Java objects (`User`, `LoginRequest`, `LoginResponse`), not raw JSON strings. Jackson handles conversion automatically. This gives type safety, IDE autocomplete, and compile-time error detection.

### JSON Schema Validation
Each endpoint has a schema file defining required fields, data types, and formats. One assertion (`matchesJsonSchemaInClasspath()`) validates the entire response structure, catching regressions that field-by-field assertions would miss.

### Config-Driven with CLI Override
API key, base URL, and timeouts live in `config.properties`. Any value can be overridden at runtime: `mvn test -Dbase.url=https://staging.reqres.in`

### Allure Request/Response Logging
The `AllureRestAssured` filter automatically captures every HTTP request and response in the report — full URL, headers, body, and status code. No manual logging code needed.

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+
- A free Reqres API key ([get one here](https://reqres.in/signup))

### Setup
Add your API key to `src/test/resources/config/config.properties`:
```properties
api.key=YOUR_API_KEY_HERE
```

### Run all tests
```bash
mvn clean test
```

### Run specific test class
```bash
mvn test -Dtest=UserTests
mvn test -Dtest=AuthTests
```

### Generate Allure report
```bash
mvn allure:serve
```

---

## Test Coverage

### User API (11 tests)
- ✅ GET list of users with pagination
- ✅ GET single user by ID
- ✅ GET non-existent user (404 handling)
- ✅ POST create new user
- ✅ PUT full update of existing user
- ✅ PATCH partial update of existing user
- ✅ DELETE user
- ✅ JSON Schema validation for list, single user, and create responses

### Authentication API (5 tests)
- ✅ Successful login returns token
- ✅ Successful registration returns id and token
- ✅ Login without password (400 error handling)
- ✅ Login without email (400 error handling)
- ✅ Registration without password (400 error handling)

---

## Framework Features

- **RestAssured Fluent API** — given/when/then syntax for readable test code
- **Parallel Execution** — TestNG runs test methods concurrently
- **Dual Assertion Styles** — Hamcrest matchers and TestNG Assert demonstrated
- **JSON Schema Validation** — structural validation beyond field-level checks
- **POJO Serialization** — type-safe request/response handling via Jackson
- **Allure Reporting** — automatic request/response capture in HTML reports
- **Config Override** — switch environments via CLI without code changes

---

## Author

**Rose Mary Joseph**
QA Automation Engineer | Selenium · Java · RestAssured · TestNG · CI/CD
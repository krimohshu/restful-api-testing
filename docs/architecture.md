# Architecture Documentation

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Test Execution Layer                          │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │  TestRunner.java (JUnit Platform Suite)                        │ │
│  │  - Discovers and executes Cucumber scenarios                   │ │
│  │  - Generates HTML and JSON reports                             │ │
│  └────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      BDD/Cucumber Layer                              │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │  Features (Gherkin Scenarios)                                  │ │
│  │  - object-management.feature                                   │ │
│  │  - Business-readable test scenarios                            │ │
│  └────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      Step Definitions Layer                          │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐    │
│  │  GivenSteps.java│  │  WhenSteps.java │  │  ThenSteps.java │    │
│  │  - Setup        │  │  - Actions      │  │  - Assertions   │    │
│  │  - Preconditions│  │  - API calls    │  │  - Validations  │    │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘    │
│           │                    │                    │                │
│           └────────────────────┼────────────────────┘                │
│                                ▼                                     │
│                    ┌─────────────────────┐                          │
│                    │  TestContext.java   │                          │
│                    │  - Shared state     │                          │
│                    │  - Object tracking  │                          │
│                    │  - Response storage │                          │
│                    └─────────────────────┘                          │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      Test Infrastructure Layer                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │  TestHooks.java                                                 │ │
│  │  - @Before: Initialize test context                            │ │
│  │  - @After: Cleanup created objects, logging                    │ │
│  └────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      API Client Layer                                │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │  RestApiClient.java                                             │ │
│  │  - REST Assured wrapper                                         │ │
│  │  - HTTP methods (GET, POST, PUT, DELETE)                       │ │
│  │  - Request/Response handling                                    │ │
│  └────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      Model Layer                                     │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │  ApiObject.java                                                 │ │
│  │  - Data structure representation                                │ │
│  │  - JSON serialization/deserialization                           │ │
│  └────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
                  ┌───────────────────────┐
                  │   RESTful API         │
                  │  (restful-api.dev)    │
                  │  - CRUD operations    │
                  └───────────────────────┘
```

## Component Descriptions

### 1. Test Execution Layer
- **TestRunner.java**: JUnit Platform Suite that discovers and executes all Cucumber scenarios
- Generates test reports in HTML and JSON formats
- Configures Cucumber plugins and features path

### 2. BDD/Cucumber Layer
- **Features**: Contains Gherkin scenarios in plain English
- Business-readable specifications
- Tagged scenarios for selective execution (@smoke, @create, @negative, etc.)

### 3. Step Definitions Layer
- **GivenSteps.java**: Preconditions and test data setup
- **WhenSteps.java**: Actions and API interactions
- **ThenSteps.java**: Assertions and validations
- **TestContext.java**: Shared state container using Dependency Injection

### 4. Test Infrastructure Layer
- **TestHooks.java**: Setup and teardown operations
  - Before hooks: Initialize test context
  - After hooks: Cleanup test data, logging, error reporting

### 5. API Client Layer
- **RestApiClient.java**: Abstraction over REST Assured
- Encapsulates all HTTP operations
- Handles request configuration and response extraction

### 6. Model Layer
- **ApiObject.java**: POJO representing API data structure
- Jackson annotations for JSON mapping
- Supports dynamic data attributes

## Data Flow

```
Feature File → Step Definition → TestContext → RestApiClient → API
                                      ↓              ↓
                                 Shared State    Response
                                      ↓              ↓
                              Step Definition ← RestApiClient
                                      ↓
                                  Assertions
```

## Key Design Patterns

### 1. **Page Object Pattern (adapted for APIs)**
- `RestApiClient` encapsulates all API operations
- Promotes reusability and maintainability

### 2. **Dependency Injection**
- Cucumber PicoContainer manages object lifecycle
- TestContext shared across step definitions

### 3. **Builder Pattern**
- `ApiObject` supports flexible data attribute construction

### 4. **Test Context Pattern**
- Shares data between steps within a scenario
- Maintains test isolation between scenarios

### 5. **Hook Pattern**
- Automatic setup and cleanup
- Consistent test environment

## Test Isolation Strategy

1. **Before Each Scenario**:
   - Reset test context
   - Initialize API client
   - Clear previous test data

2. **During Scenario**:
   - Track all created object IDs
   - Maintain response history
   - Share state via TestContext

3. **After Each Scenario**:
   - Delete all created objects
   - Log scenario results
   - Clean up test context

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| BDD Framework | Cucumber | 7.14.0 |
| Test Runner | JUnit 5 | 5.10.0 |
| API Client | REST Assured | 5.3.2 |
| Assertions | Hamcrest | 2.2 |
| JSON Processing | Jackson | 2.15.3 |
| DI Container | Cucumber PicoContainer | 7.14.0 |
| Build Tool | Maven | 3.6+ |
| Java | JDK | 11+ |

## Test Execution Flow

```
1. Maven Surefire Plugin (Parallel: 4 threads)
        ↓
2. JUnit Platform Engine
        ↓
3. Cucumber Engine (Parallel execution enabled)
        ↓
4. Feature File Discovery
        ↓
5. Scenario Execution (in parallel)
        ↓
6. Hook: @Before
        ↓
7. Step Definitions Execution
   - Given (Setup)
   - When (Action)
   - Then (Assert)
        ↓
8. Hook: @After (Cleanup)
        ↓
9. Report Generation
```

### Parallel Execution Architecture

**Performance:** 60% faster execution (24s → 10s)

```
Maven Surefire Plugin
        ↓
   Thread Pool (4 threads)
   ┌─────┬─────┬─────┬─────┐
   │ T1  │ T2  │ T3  │ T4  │
   └──┬──┴──┬──┴──┬──┴──┬──┘
      │     │     │     │
   Scenario Scenario Scenario Scenario
      ↓     ↓     ↓     ↓
   TestContext (isolated per thread)
      ↓     ↓     ↓     ↓
   Independent API Calls
```

**Configuration:**
- Dynamic thread allocation based on CPU cores
- Thread-safe TestContext via PicoContainer
- No shared state between scenarios
- Automatic cleanup per thread

## Configuration

### config.properties
```properties
api.base.url=https://api.restful-api.dev
test.timeout.seconds=30
test.cleanup.enabled=true
logging.level=INFO
```

### pom.xml (Key Dependencies & Parallel Config)
**Dependencies:**
- REST Assured 5.3.2 for API testing
- Cucumber 7.14.0 for BDD
- JUnit 5.10.0 for test execution
- Jackson 2.15.3 for JSON handling
- Hamcrest 2.2 for assertions

**Parallel Execution:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>4</threadCount>
        <perCoreThreadCount>true</perCoreThreadCount>
        <properties>
            <configurationParameters>
                cucumber.execution.parallel.enabled=true
                cucumber.execution.parallel.config.strategy=dynamic
            </configurationParameters>
        </properties>
    </configuration>
</plugin>
```

### junit-platform.properties
```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.strategy=dynamic
cucumber.execution.parallel.config.dynamic.factor=1.0
```

## Report Generation

### HTML Report
- Location: `target/cucumber-reports/cucumber.html`
- Interactive UI with test results
- Scenario details with steps

### JSON Report
- Location: `target/cucumber-reports/cucumber.json`
- Machine-readable format
- CI/CD integration friendly

### Console Output
- Real-time test execution logs
- Request/Response details
- Scenario status

## Best Practices Implemented

1. ✅ **Test Isolation**: Each scenario is independent (thread-safe)
2. ✅ **Parallel Execution**: 4-thread concurrent execution (60% faster)
3. ✅ **Automatic Cleanup**: Created objects are deleted after each test
4. ✅ **Thread Safety**: PicoContainer ensures proper DI per thread
5. ✅ **Clear Naming**: Descriptive method and variable names
6. ✅ **Separation of Concerns**: Each layer has single responsibility
7. ✅ **Reusability**: Step definitions can be reused
8. ✅ **Maintainability**: Easy to add new scenarios
9. ✅ **Error Handling**: Comprehensive negative testing
10. ✅ **Logging**: Detailed execution logs for debugging

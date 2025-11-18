# Test Execution Guide

## Quick Commands

### Basic Execution
```bash
# Run all tests
mvn clean test

# Run with fresh build
mvn clean install test
```

### Tag-Based Execution

#### By Feature
```bash
# Create operations
mvn test -Dcucumber.filter.tags="@create"

# Retrieve operations
mvn test -Dcucumber.filter.tags="@retrieve"

# Update operations
mvn test -Dcucumber.filter.tags="@update"

# Delete operations
mvn test -Dcucumber.filter.tags="@delete"

# List operations
mvn test -Dcucumber.filter.tags="@list"
```

#### By Test Type
```bash
# Smoke tests (fast, critical paths)
mvn test -Dcucumber.filter.tags="@smoke"

# Negative tests (error handling)
mvn test -Dcucumber.filter.tags="@negative"

# Edge cases
mvn test -Dcucumber.filter.tags="@edge-case"

# Data integrity tests
mvn test -Dcucumber.filter.tags="@data-integrity"
```

#### Combined Tags
```bash
# Smoke AND create tests
mvn test -Dcucumber.filter.tags="@smoke and @create"

# Create OR retrieve tests
mvn test -Dcucumber.filter.tags="@create or @retrieve"

# All except work-in-progress
mvn test -Dcucumber.filter.tags="not @wip"

# Negative tests for create/delete
mvn test -Dcucumber.filter.tags="@negative and (@create or @delete)"
```

## Interactive Execution

Use the provided shell script for interactive menu:
```bash
./run-tests.sh
```

## Viewing Reports

### HTML Report (Recommended)
```bash
# macOS
open target/cucumber-reports/cucumber.html

# Linux
xdg-open target/cucumber-reports/cucumber.html

# Windows
start target/cucumber-reports/cucumber.html
```

### JSON Report (CI/CD Integration)
```bash
cat target/cucumber-reports/cucumber.json
```

## Console Output

### Standard Verbosity
```bash
mvn test
```

### Debug Mode
```bash
mvn test -X
```

### Quiet Mode
```bash
mvn test -q
```

## Test Results Location

After execution, find results at:
- **HTML Report**: `target/cucumber-reports/cucumber.html`
- **JSON Report**: `target/cucumber-reports/cucumber.json`
- **Maven Logs**: `target/surefire-reports/`

## Continuous Integration

### Jenkins
```groovy
stage('API Tests') {
    steps {
        sh 'mvn clean test'
        cucumber reportTitle: 'API Test Report',
                fileIncludePattern: '**/cucumber.json',
                jsonReportDirectory: 'target/cucumber-reports'
    }
}
```

### GitHub Actions
```yaml
- name: Run API Tests
  run: mvn clean test

- name: Upload Test Report
  uses: actions/upload-artifact@v3
  with:
    name: cucumber-report
    path: target/cucumber-reports/
```

## Test Scenarios Summary

Total Scenarios: **20+**

### By Category:
- ✅ **Smoke Tests**: 2 scenarios
- ✅ **CRUD Operations**: 5 scenarios
- ✅ **Edge Cases**: 5 scenarios
- ✅ **Error Handling**: 4 scenarios
- ✅ **Data Integrity**: 2 scenarios
- ✅ **Advanced Flows**: 2+ scenarios

### Coverage:
- ✅ Create objects (single and multiple)
- ✅ Retrieve objects by ID
- ✅ List all objects
- ✅ Update objects
- ✅ Delete objects
- ✅ Special characters handling
- ✅ Boundary values (zero, large numbers)
- ✅ Empty data handling
- ✅ Non-existent resources (404)
- ✅ Invalid requests (400)
- ✅ Data persistence validation
- ✅ Complete CRUD lifecycle

## Troubleshooting

### Issue: Connection timeout
**Solution**: Check internet connection and API availability at https://api.restful-api.dev

### Issue: Build failure
**Solution**: 
```bash
mvn clean install -U
```

### Issue: Tests not found
**Solution**: Verify feature files exist in `src/test/resources/features/`

### Issue: Dependencies not resolved
**Solution**:
```bash
mvn dependency:resolve
mvn dependency:tree
```

## Best Practices

1. **Run smoke tests first** for quick validation
2. **Use tags** to run specific test subsets
3. **Check reports** after each run
4. **Clean before important runs**: `mvn clean test`
5. **Use CI/CD** for automated execution

## Performance Tips

- Smoke tests: ~30 seconds
- Full suite: ~2-3 minutes
- Individual scenarios: ~1-5 seconds each

## Next Steps

1. Execute smoke tests to verify setup
2. Review generated HTML report
3. Explore individual scenarios
4. Modify scenarios as needed
5. Add custom scenarios for specific use cases

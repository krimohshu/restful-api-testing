package com.api.test.steps;

import com.api.test.context.TestContext;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ThenSteps {
    
    private final TestContext context;
    
    public ThenSteps(TestContext context) {
        this.context = context;
    }
    
    @Then("a {int} response code is returned")
    public void aResponseCodeIsReturned(int expectedStatusCode) {
        int actualStatusCode = context.getCurrentResponse().getStatusCode();
        System.out.println("🔍 DEBUG: Verifying status code");
        System.out.println("   Expected: " + expectedStatusCode);
        System.out.println("   Actual: " + actualStatusCode);
        
        if (actualStatusCode != expectedStatusCode) {
            System.out.println("❌ DEBUG: Status code mismatch!");
            System.out.println("   Response body: " + context.getCurrentResponse().getBody().asString());
        } else {
            System.out.println("✅ DEBUG: Status code matches!");
        }
        
        assertThat("Response status code should be " + expectedStatusCode,
                actualStatusCode, equalTo(expectedStatusCode));
    }
    
    @Then("the response contains a valid object id")
    public void theResponseContainsAValidObjectId() {
        // Use path() instead of jsonPath() for thread-safety
        String id = context.getCurrentResponse().path("id");
        assertThat("Response should contain an id", id, notNullValue());
        assertThat("Id should not be empty", id, not(emptyString()));
    }
    
    @Then("a {string} is created")
    public void aIsCreated(String expectedName) {
        String actualName = context.getCurrentResponse().path("name");
        assertThat("Object name should match", actualName, equalTo(expectedName));
    }
    
    @Then("a {string} is returned")
    public void aIsReturned(String expectedName) {
        String actualName = context.getCurrentResponse().path("name");
        assertThat("Object name should match", actualName, equalTo(expectedName));
    }
    
    @Then("the object has property {string} with value {string}")
    public void theObjectHasPropertyWithStringValue(String jsonPath, String expectedValue) {
        // Handle JSON paths with spaces by using bracket notation
        String actualValue;
        if (jsonPath.contains(" ")) {
            // For paths like "data.CPU model", we need to access it differently
            String[] parts = jsonPath.split("\\.");
            if (parts.length == 2 && parts[0].equals("data")) {
                Map<String, Object> data = context.getCurrentResponse().path("data");
                actualValue = data != null ? String.valueOf(data.get(parts[1])) : null;
            } else {
                actualValue = context.getCurrentResponse().path(jsonPath);
            }
        } else {
            actualValue = context.getCurrentResponse().path(jsonPath);
        }
        assertThat("Property " + jsonPath + " should match", actualValue, equalTo(expectedValue));
    }
    
    @Then("the object has property {string} with value {double}")
    public void theObjectHasPropertyWithDoubleValue(String jsonPath, double expectedValue) {
        Object actualValue = context.getCurrentResponse().path(jsonPath);
        
        // Handle both Double, Float, and Integer responses
        double actualDouble;
        if (actualValue instanceof Integer) {
            actualDouble = ((Integer) actualValue).doubleValue();
        } else if (actualValue instanceof Double) {
            actualDouble = (Double) actualValue;
        } else if (actualValue instanceof Float) {
            actualDouble = ((Float) actualValue).doubleValue();
        } else if (actualValue instanceof String) {
            actualDouble = Double.parseDouble((String) actualValue);
        } else {
            throw new AssertionError("Unexpected type for property " + jsonPath + ": " + 
                    (actualValue != null ? actualValue.getClass() : "null"));
        }
        
        assertThat("Property " + jsonPath + " should match", actualDouble, closeTo(expectedValue, 0.01));
    }
    
    @Then("the object has all specified attributes")
    public void theObjectHasAllSpecifiedAttributes() {
        Map<String, Object> expectedData = context.getCurrentObject().getData();
        Map<String, Object> actualData = context.getCurrentResponse().path("data");
        
        assertThat("Response should contain data", actualData, notNullValue());
        
        for (Map.Entry<String, Object> entry : expectedData.entrySet()) {
            String key = entry.getKey();
            Object expectedValue = entry.getValue();
            Object actualValue = actualData.get(key);
            
            assertThat("Data should contain key: " + key, actualData, hasKey(key));
            
            // Handle numeric comparison
            if (expectedValue instanceof Number && actualValue instanceof Number) {
                double expected = ((Number) expectedValue).doubleValue();
                double actual = ((Number) actualValue).doubleValue();
                assertThat("Value for " + key + " should match", actual, closeTo(expected, 0.01));
            } else {
                assertThat("Value for " + key + " should match", actualValue, equalTo(expectedValue));
            }
        }
    }
    
    @Then("the response contains at least {int} objects")
    public void theResponseContainsAtLeastObjects(int minCount) {
        List<Object> objects = context.getCurrentResponse().path("$");
        assertThat("Response should contain at least " + minCount + " objects",
                objects.size(), greaterThanOrEqualTo(minCount));
    }
    
    @Then("the list includes {string}")
    public void theListIncludes(String itemName) {
        List<String> names = context.getCurrentResponse().path("name");
        assertThat("List should include " + itemName, names, hasItem(itemName));
    }
    
    @Then("the response contains a success message")
    public void theResponseContainsASuccessMessage() {
        String message = context.getCurrentResponse().path("message");
        assertThat("Response should contain a message", message, notNullValue());
    }
    
    @Then("the response contains an error message")
    public void theResponseContainsAnErrorMessage() {
        String error = context.getCurrentResponse().path("error");
        assertThat("Response should contain an error", error, notNullValue());
    }
    
    @Then("the response contains a validation error")
    public void theResponseContainsAValidationError() {
        String error = context.getCurrentResponse().path("error");
        assertThat("Response should contain a validation error", error, notNullValue());
    }
    
    @Then("the object name contains special characters")
    public void theObjectNameContainsSpecialCharacters() {
        String name = context.getCurrentResponse().path("name");
        assertThat("Name should contain special characters", 
                name, anyOf(containsString("&"), containsString("<"), containsString(">"), containsString("\"")));
    }
    
    @Then("both responses have identical data")
    public void bothResponsesHaveIdenticalData() {
        Map<String, Object> previousData = context.getPreviousResponse().path("data");
        Map<String, Object> currentData = context.getCurrentResponse().path("data");
        
        assertThat("Both responses should have identical data", currentData, equalTo(previousData));
    }
    
    @Then("both responses contain the same object id")
    public void bothResponsesContainTheSameObjectId() {
        String previousId = context.getPreviousResponse().path("id");
        String currentId = context.getCurrentResponse().path("id");
        
        assertThat("Both responses should have the same id", currentId, equalTo(previousId));
    }
    
    @Then("the response is a valid JSON array")
    public void theResponseIsAValidJsonArray() {
        List<Object> array = context.getCurrentResponse().path("$");
        assertThat("Response should be a valid array", array, notNullValue());
    }
    
    @Then("each object in the list has required fields")
    public void eachObjectInTheListHasRequiredFields() {
        List<Map<String, Object>> objects = context.getCurrentResponse().path("$");
        
        for (Map<String, Object> obj : objects) {
            assertThat("Object should have 'id' field", obj, hasKey("id"));
            assertThat("Object should have 'name' field", obj, hasKey("name"));
        }
    }
}

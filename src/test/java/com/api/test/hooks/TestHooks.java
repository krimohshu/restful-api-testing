package com.api.test.hooks;

import com.api.test.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class TestHooks {
    
    private final TestContext context;
    
    public TestHooks(TestContext context) {
        this.context = context;
    }
    
    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("===================================================");
        System.out.println("Starting Scenario: " + scenario.getName());
        System.out.println("Tags: " + scenario.getSourceTagNames());
        System.out.println("===================================================");
        
        // Reset context for new scenario
        context.reset();
    }
    
    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("===================================================");
        System.out.println("Completed Scenario: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());
        
        // Cleanup created objects
        cleanupCreatedObjects();
        
        if (scenario.isFailed()) {
            System.out.println("Scenario FAILED!");
            if (context.getCurrentResponse() != null) {
                System.out.println("Last Response Status: " + context.getCurrentResponse().getStatusCode());
                System.out.println("Last Response Body: " + context.getCurrentResponse().getBody().asString());
            }
        }
        
        System.out.println("===================================================\n");
        
        // Final cleanup
        context.cleanup();
    }
    
    /**
     * Cleanup all objects created during the test
     */
    private void cleanupCreatedObjects() {
        List<String> objectIds = new ArrayList<>(context.getCreatedObjectIds());
        
        if (!objectIds.isEmpty()) {
            System.out.println("Cleaning up " + objectIds.size() + " created objects...");
            
            for (String id : objectIds) {
                try {
                    Response response = context.getApiClient().deleteObject(id);
                    if (response.getStatusCode() == 200) {
                        System.out.println("Successfully deleted object: " + id);
                    } else {
                        System.out.println("Failed to delete object " + id + " (might be already deleted)");
                    }
                } catch (Exception e) {
                    System.out.println("Error deleting object " + id + ": " + e.getMessage());
                }
            }
        }
    }
}

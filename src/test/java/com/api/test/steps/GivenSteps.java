package com.api.test.steps;

import com.api.test.context.TestContext;
import com.api.test.model.ApiObject;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

public class GivenSteps {
    
    private final TestContext context;
    
    public GivenSteps(TestContext context) {
        this.context = context;
    }
    
    @Given("the API base URL is {string}")
    public void theApiBaseUrlIs(String baseUrl) {
        System.out.println("🔍 DEBUG: Setting API base URL to: " + baseUrl);
        context.setApiClient(new com.api.test.client.RestApiClient(baseUrl));
        System.out.println("✅ DEBUG: API client initialized successfully");
    }
    
    @Given("a {string} item is created")
    public void anItemIsCreated(String itemName) {
        System.out.println("🔍 DEBUG: Creating item with name: " + itemName);
        ApiObject apiObject = new ApiObject(itemName);
        context.setCurrentObject(apiObject);
        System.out.println("✅ DEBUG: Item object created and stored in context");
    }
    
    @Given("is a {string} CPU model")
    public void isACpuModel(String cpuModel) {
        context.getCurrentObject().addDataAttribute("CPU model", cpuModel);
    }
    
    @Given("has a price of {string}")
    public void hasAPriceOf(String price) {
        System.out.println("🔍 DEBUG: Setting price to: " + price);
        double priceValue = Double.parseDouble(price);
        context.getCurrentObject().addDataAttribute("price", priceValue);
        System.out.println("✅ DEBUG: Price added to object data");
    }
    
    @Given("has the following attributes:")
    public void hasTheFollowingAttributes(DataTable dataTable) {
        Map<String, String> attributes = dataTable.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            // Try to parse as number, otherwise keep as string
            Object value;
            try {
                value = Double.parseDouble(entry.getValue());
            } catch (NumberFormatException e) {
                value = entry.getValue();
            }
            context.getCurrentObject().addDataAttribute(entry.getKey(), value);
        }
    }
    
    @Given("has empty data attributes")
    public void hasEmptyDataAttributes() {
        // Data map is already empty by default, just ensure it's clear
        context.getCurrentObject().setData(new java.util.HashMap<>());
    }
    
    @Given("an object with no name is created")
    public void anObjectWithNoNameIsCreated() {
        ApiObject apiObject = new ApiObject();
        apiObject.setName(null);
        context.setCurrentObject(apiObject);
    }
    
    @Given("the following items are created:")
    public void theFollowingItemsAreCreated(DataTable dataTable) {
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> item : items) {
            ApiObject apiObject = new ApiObject(item.get("name"));
            
            if (item.containsKey("CPU model")) {
                apiObject.addDataAttribute("CPU model", item.get("CPU model"));
            }
            if (item.containsKey("price")) {
                apiObject.addDataAttribute("price", Double.parseDouble(item.get("price")));
            }
            
            // Create the object
            context.setCurrentObject(apiObject);
            context.setCurrentResponse(context.getApiClient().createObject(apiObject));
            
            // Store the created object ID for cleanup
            String id = context.getCurrentResponse().path("id");
            context.addCreatedObjectId(id);
        }
    }
}

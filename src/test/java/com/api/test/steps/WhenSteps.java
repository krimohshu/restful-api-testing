package com.api.test.steps;

import com.api.test.context.TestContext;
import com.api.test.model.ApiObject;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.Map;

public class WhenSteps {
    
    private final TestContext context;
    
    public WhenSteps(TestContext context) {
        this.context = context;
    }
    
    @When("the request to add the item is made")
    public void theRequestToAddTheItemIsMade() {
        Response response = context.getApiClient().createObject(context.getCurrentObject());
        context.setCurrentResponse(response);
        
        // If creation was successful, store the ID for cleanup
        if (response.getStatusCode() == 200) {
            String id = response.path("id");
            context.addCreatedObjectId(id);
        }
    }
    
    @When("I retrieve the created object by its id")
    public void iRetrieveTheCreatedObjectByItsId() {
        String objectId = context.getCurrentResponse().path("id");
        Response response = context.getApiClient().getObjectById(objectId);
        context.setCurrentResponse(response);
    }
    
    @When("I retrieve the same object again")
    public void iRetrieveTheSameObjectAgain() {
        String objectId = context.getCurrentResponse().path("id");
        Response response = context.getApiClient().getObjectById(objectId);
        context.setCurrentResponse(response);
    }
    
    @When("I request to list all objects")
    public void iRequestToListAllObjects() {
        Response response = context.getApiClient().getAllObjects();
        context.setCurrentResponse(response);
    }
    
    @When("I delete the created object")
    public void iDeleteTheCreatedObject() {
        String objectId = context.getCurrentResponse().path("id");
        Response response = context.getApiClient().deleteObject(objectId);
        context.setCurrentResponse(response);
    }
    
    @When("I attempt to retrieve the deleted object")
    public void iAttemptToRetrieveTheDeletedObject() {
        // Get the ID before it was deleted (from test data or previous response)
        String objectId = context.getCreatedObjectIds().get(context.getCreatedObjectIds().size() - 1);
        Response response = context.getApiClient().getObjectById(objectId);
        context.setCurrentResponse(response);
    }
    
    @When("I update the object with name {string}")
    public void iUpdateTheObjectWithName(String newName) {
        String objectId = context.getCurrentResponse().path("id");
        ApiObject updateObject = new ApiObject(newName);
        
        // Preserve existing data attributes
        Map<String, Object> existingData = context.getCurrentResponse().path("data");
        if (existingData != null) {
            updateObject.setData(existingData);
        }
        
        Response response = context.getApiClient().updateObject(objectId, updateObject);
        context.setCurrentResponse(response);
    }
    
    @When("I update the object with price {string}")
    public void iUpdateTheObjectWithPrice(String price) {
        String objectId = context.getCurrentResponse().path("id");
        String name = context.getCurrentResponse().path("name");
        
        ApiObject updateObject = new ApiObject(name);
        Map<String, Object> existingData = context.getCurrentResponse().path("data");
        if (existingData != null) {
            updateObject.setData(new java.util.HashMap<>(existingData));
        }
        updateObject.addDataAttribute("price", Double.parseDouble(price));
        
        Response response = context.getApiClient().updateObject(objectId, updateObject);
        context.setCurrentResponse(response);
    }
    
    @When("I attempt to retrieve an object with id {string}")
    public void iAttemptToRetrieveAnObjectWithId(String id) {
        Response response = context.getApiClient().getObjectById(id);
        context.setCurrentResponse(response);
    }
    
    @When("I attempt to delete an object with id {string}")
    public void iAttemptToDeleteAnObjectWithId(String id) {
        Response response = context.getApiClient().deleteObject(id);
        context.setCurrentResponse(response);
    }
    
    @When("I send an invalid JSON request to create an object")
    public void iSendAnInvalidJsonRequestToCreateAnObject() {
        String invalidJson = "{invalid json structure}";
        Response response = context.getApiClient().createObjectWithInvalidJson(invalidJson);
        context.setCurrentResponse(response);
    }
}

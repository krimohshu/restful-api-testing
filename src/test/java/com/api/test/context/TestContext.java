package com.api.test.context;

import com.api.test.client.RestApiClient;
import com.api.test.model.ApiObject;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test context to share data between steps
 */
public class TestContext {
    
    private RestApiClient apiClient;
    private ApiObject currentObject;
    private Response currentResponse;
    private Response previousResponse;
    private List<String> createdObjectIds;
    private Map<String, Object> testData;
    
    public TestContext() {
        this.createdObjectIds = new ArrayList<>();
        this.testData = new HashMap<>();
    }
    
    public RestApiClient getApiClient() {
        return apiClient;
    }
    
    public void setApiClient(RestApiClient apiClient) {
        this.apiClient = apiClient;
    }
    
    public ApiObject getCurrentObject() {
        return currentObject;
    }
    
    public void setCurrentObject(ApiObject currentObject) {
        this.currentObject = currentObject;
    }
    
    public Response getCurrentResponse() {
        return currentResponse;
    }
    
    public void setCurrentResponse(Response response) {
        this.previousResponse = this.currentResponse;
        this.currentResponse = response;
    }
    
    public Response getPreviousResponse() {
        return previousResponse;
    }
    
    public void addCreatedObjectId(String id) {
        if (id != null && !id.isEmpty()) {
            this.createdObjectIds.add(id);
        }
    }
    
    public List<String> getCreatedObjectIds() {
        return createdObjectIds;
    }
    
    public void setTestData(String key, Object value) {
        this.testData.put(key, value);
    }
    
    public Object getTestData(String key) {
        return this.testData.get(key);
    }
    
    public void clearTestData() {
        this.testData.clear();
    }
    
    public void reset() {
        this.currentObject = null;
        this.currentResponse = null;
        this.previousResponse = null;
        this.testData.clear();
    }
    
    public void cleanup() {
        // Cleanup is handled by hooks
        this.createdObjectIds.clear();
        reset();
    }
}

package com.api.test.client;

import com.api.test.model.ApiObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestApiClient {
    
    private String baseUrl;
    private static final String OBJECTS_ENDPOINT = "/objects";
    
    public RestApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        RestAssured.baseURI = baseUrl;
    }
    
    private RequestSpecification getRequestSpec() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all();
    }
    
    /**
     * Create a new object
     */
    public Response createObject(ApiObject apiObject) {
        return getRequestSpec()
                .body(apiObject)
                .when()
                .post(OBJECTS_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }
    
    /**
     * Create an object with invalid JSON
     */
    public Response createObjectWithInvalidJson(String invalidJson) {
        return getRequestSpec()
                .body(invalidJson)
                .when()
                .post(OBJECTS_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }
    
    /**
     * Get object by ID
     */
    public Response getObjectById(String id) {
        return getRequestSpec()
                .when()
                .get(OBJECTS_ENDPOINT + "/" + id)
                .then()
                .log().all()
                .extract().response();
    }
    
    /**
     * Get all objects
     */
    public Response getAllObjects() {
        return getRequestSpec()
                .when()
                .get(OBJECTS_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }
    
    /**
     * Update an existing object (PUT)
     */
    public Response updateObject(String id, ApiObject apiObject) {
        return getRequestSpec()
                .body(apiObject)
                .when()
                .put(OBJECTS_ENDPOINT + "/" + id)
                .then()
                .log().all()
                .extract().response();
    }
    
    /**
     * Partially update an object (PATCH)
     */
    public Response patchObject(String id, ApiObject apiObject) {
        return getRequestSpec()
                .body(apiObject)
                .when()
                .patch(OBJECTS_ENDPOINT + "/" + id)
                .then()
                .log().all()
                .extract().response();
    }
    
    /**
     * Delete an object
     */
    public Response deleteObject(String id) {
        return getRequestSpec()
                .when()
                .delete(OBJECTS_ENDPOINT + "/" + id)
                .then()
                .log().all()
                .extract().response();
    }
    
    /**
     * Get base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }
}

Feature: Object Management via RESTful API
  As a user of the RESTful API
  I want to create, retrieve, update, and delete objects
  So that I can manage my data effectively

  Background:
    Given the API base URL is "https://api.restful-api.dev"

  @smoke @create @current_test_to_debug
  Scenario: Verify an item can be created with valid data
    Given a "Apple MacBook Pro 16" item is created
    And is a "Intel Core i9" CPU model
    And has a price of "1849.99"
    # When the request to add the item is made
    # Then a 200 response code is returned
    # And the response contains a valid object id
    # And a "Apple MacBook Pro 16" is created
    # And the object has property "data.CPU model" with value "Intel Core i9"
    # And the object has property "data.price" with value 1849.99

  @smoke @create
  Scenario: Create an object with multiple attributes
    Given a "Dell XPS 15" item is created
    And has the following attributes:
      | CPU model     | Intel Core i7  |
      | Hard disk size| 1 TB           |
      | RAM size      | 32 GB          |
      | price         | 1599.99        |
    When the request to add the item is made
    # Then a 200 response code is returned
    # And the response contains a valid object id
    # And a "Dell XPS 15" is created
    # And the object has all specified attributes

  # @retrieve
  # Scenario: Ability to return a created item
  #   Given a "Samsung Galaxy S23" item is created
  #   And has a price of "899.99"
  #   When the request to add the item is made
  #   And I retrieve the created object by its id
  #   Then a 200 response code is returned
  #   And a "Samsung Galaxy S23" is returned
  #   And the object has property "data.price" with value 899.99

  # @list
  # Scenario: Ability to list multiple items
  #   Given the following items are created:
  #     | name                  | CPU model      | price   |
  #     | HP Pavilion           | AMD Ryzen 5    | 799.99  |
  #     | Lenovo ThinkPad       | Intel Core i5  | 1199.99 |
  #     | ASUS ROG              | Intel Core i9  | 2199.99 |
  #   When I request to list all objects
  #   Then a 200 response code is returned
  #   And the response contains at least 3 objects

  # @delete
  # Scenario: Successfully delete an existing object
  #   Given a "Microsoft Surface Pro" item is created
  #   And has a price of "999.99"
  #   When the request to add the item is made
  #   And I delete the created object
  #   Then a 200 response code is returned
  #   And the response contains a success message
  #   When I attempt to retrieve the deleted object
  #   Then a 404 response code is returned

  # @update
  # Scenario: Update an existing object
  #   Given a "iPhone 14" item is created
  #   And has a price of "999.00"
  #   When the request to add the item is made
  #   And I update the object with name "iPhone 15 Pro"
  #   And I update the object with price "1199.99"
  #   Then a 200 response code is returned
  #   And a "iPhone 15 Pro" is returned
  #   And the object has property "data.price" with value 1199.99

  # @edge-case
  # Scenario: Create object with empty data attributes
  #   Given a "Test Device" item is created
  #   And has empty data attributes
  #   When the request to add the item is made
  #   Then a 200 response code is returned
  #   And the response contains a valid object id
  #   And a "Test Device" is created

  # @edge-case
  # Scenario: Create object with special characters in name
  #   Given a "Test & Device <Special> \"Chars\"" item is created
  #   And has a price of "100.00"
  #   When the request to add the item is made
  #   Then a 200 response code is returned
  #   And the object name contains special characters

  # @edge-case
  # Scenario: Create object with very large price
  #   Given a "Luxury Item" item is created
  #   And has a price of "9999999.99"
  #   When the request to add the item is made
  #   Then a 200 response code is returned
  #   And the object has property "data.price" with value 9999999.99

  # @edge-case
  # Scenario: Create object with zero price
  #   Given a "Free Item" item is created
  #   And has a price of "0.00"
  #   When the request to add the item is made
  #   Then a 200 response code is returned
  #   And the object has property "data.price" with value 0.00

  # @negative @error-handling
  # Scenario: Attempt to retrieve non-existent object
  #   When I attempt to retrieve an object with id "99999999999"
  #   Then a 404 response code is returned
  #   And the response contains an error message

  # @negative @error-handling
  # Scenario: Attempt to delete non-existent object
  #   When I attempt to delete an object with id "99999999999"
  #   Then a 404 response code is returned
  #   And the response contains an error message

  # @negative @error-handling
  # Scenario: Create object with missing name field
  #   Given an object with no name is created
  #   And has a price of "500.00"
  #   When the request to add the item is made
  #   Then a 200 response code is returned
  #   And the response contains a valid object id

  # @negative @error-handling
  # Scenario: Create object with invalid JSON structure
  #   When I send an invalid JSON request to create an object
  #   Then a 400 response code is returned

  # @data-integrity
  # Scenario: Verify object persistence across multiple retrievals
  #   Given a "Consistency Test Device" item is created
  #   And has a price of "555.55"
  #   When the request to add the item is made
  #   And I retrieve the created object by its id
  #   And I retrieve the same object again
  #   Then both responses have identical data
  #   And both responses contain the same object id

  # @list @pagination
  # Scenario: List objects contains valid response structure
  #   When I request to list all objects
  #   Then a 200 response code is returned
  #   And the response is a valid JSON array
  #   And each object in the list has required fields

  # @chaining
  # Scenario: Complete CRUD lifecycle of an object
  #   Given a "Lifecycle Test Device" item is created
  #   And has a price of "777.77"
  #   When the request to add the item is made
  #   Then a 200 response code is returned
  #   When I retrieve the created object by its id
  #   Then a 200 response code is returned
  #   And a "Lifecycle Test Device" is returned
  #   When I update the object with name "Updated Lifecycle Device"
  #   Then a 200 response code is returned
  #   And a "Updated Lifecycle Device" is returned
  #   When I delete the created object
  #   Then a 200 response code is returned
  #   When I attempt to retrieve the deleted object
  #   Then a 404 response code is returned

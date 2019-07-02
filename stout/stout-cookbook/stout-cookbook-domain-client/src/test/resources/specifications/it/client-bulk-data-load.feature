@clientBulkDataload
Feature: Support Bulk Data Load
  
  Collections can be created, updated, and deleted to support batch operations

  Background: 
    Given the following valid data
      | data1 |
      | data2 |
      | data3 |
      | data4 |
      | data5 |
      | data6 |
      | data7 |
      | data8 |
    And the following valid and invalid data
      | good_data1 |
      | bad_data2  |
      | good_data3 |
      | good_data4 |
      | bad_data5  |
      | good_data6 |
      | bad_data7  |
      | good_data8 |
      |            |

  Scenario: Collections of data are created if all data is valid
    Given a collection of valid data items
    When the collection is sent over in bulk to be created
    Then each data value is successfully validated and created

  Scenario: Collections can be updated if all data is valid
    Given a collection of valid data items
    When the collection is sent over in bulk to be updated
    Then each data value is successfully validated and updated

  Scenario: Collections can be deleted if all data is valid
    Given a collection of valid data items
    When the collection is sent over in bulk to be deleted
    Then each data value is successfully deleted

  Scenario: Collections of data are not created if any data is invalid
    Given a collection of invalid data items
    When the valid and invalid data is sent over in bulk to be created
    Then each data value is not saved and an error is thrown

  Scenario: Collections of data are not updated if any data is invalid
    Given valid data already exists in the system
    When the valid and invalid data is sent over in bulk to be updated
    Then each data value is not updated and an error is thrown

  Scenario: Collections of data are not deleted if any data is invalid
    Given valid data already exists in the system
    When the valid and invalid data is sent over in bulk to be deleted
    Then each data value is not deleted and an error is thrown

  Scenario: The primary key is added to the message manager when a bulk update fails
    Given an object created with valid fields
    When the object is bulk updated with an invalid field
    Then a message is created with the object's primary key

  Scenario: The primary key is added to the message manager when a bulk delete fails
    Given an object created with valid fields
    When the object is bulk deleted with an invalid field
    Then a message is created with the object's primary key

  Scenario: The primary key is added to the message manager when a bulk update fails for multiple objects
    Given three objects are created with valid fields
    When two objects are bulk updated with an invalid field
    Then a message is created with the objects' primary keys

  Scenario: The primary key is added to the message manager when a bulk delete fails for multiple objects
    Given three objects are created with valid fields
    When two objects are bulk deleted after they are no longer in the database
    Then a message is created with the objects' primary keys

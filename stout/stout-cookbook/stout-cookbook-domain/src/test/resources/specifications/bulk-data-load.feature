@bulkDataload
Feature: Support Bulk Data Load
  
  Collections can be created, updated, and deleted to support batch operations

  Background: 
    Given the following valid data exists
      | validDataValues |
      | data1           |
      | data2           |
      | data3           |
      | data4           |
      | data5           |
      | data6           |
      | data7           |
      | data8           |
      #check to see what happens if you pass in an empty entity in bulk
      |                 |
    And the following valid and invalid data exists
      | mixedDataValues |
      | good_data1      |
      | good_data2      |
      | good_data3      |
      | good_data4      |
      | bad_data5       |
      | good_data6      |
      | good_data7      |
      | good_data8      |
      #check to see what happens if you pass in an empty entity in bulk
      |                 |

  Scenario: Collections of data are created if all data is valid
    When the valid data is sent over in bulk to be created
    Then each data value is created and saved

  Scenario: Collections can be updated if all data is valid
    When the valid data is sent over in bulk to be updated
    Then each data value is updated and saved

  Scenario: Collections can be deleted if all data is valid
    When the valid data is sent over to be deleted
    Then each data value is deleted

  Scenario: Collections of data are not created if any data is invalid
    When the valid and invalid data is sent over in bulk to be created
    Then each data value is not saved and an error is thrown

  Scenario: Collections of data are not updated if any data is invalid
    When the valid and invalid data is sent over in bulk to be updated
    Then each data value is not updated and an error is thrown

  Scenario: Collections of data are not deleted if any data is invalid
    When the valid and invalid data is sent over in bulk to be deleted
    Then each data value is not deleted and an error is thrown
    
  #Scenario: Empty collections of data return a HTTP 400
    #Given the following data exists
    #When the data is sent over in bulk as a CREATE, UPDATE, or DELETE
    #Then the data is not saved, updated, or deleted and a HTTP 400 is returned
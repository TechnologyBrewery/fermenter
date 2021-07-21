@enhancedToString
Feature: Data rich toString method

  Scenario Outline: Ensure all fields for the object are present in the enhanced toString output
    Given the object is type "<objectType>"
    When the system checks the fields of the toStringOutput
    Then the toStringOutput should contain all of the object fields "<fieldNames>"

    Examples: 
      | objectType                  | fieldNames                                  |
      | IdentityKeyedEntityBO       | repository,id,internalTransientID,validator |
      | TransientSubEntityExampleBO | name,requiredReference,validator            |

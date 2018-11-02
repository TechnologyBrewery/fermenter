@remoteReferenceValidation
Feature: Reference Level Validation Requirements
  
  Reference level validation checks that a referenced value exists

  #tests for remote reference
  Scenario Outline: Reference level validation passes on valid references
    Given the "<address>" has a remote reference to "<state>"
    When the reference level validation is performed on the value "<address>"
    Then the reference level validation does not return an error for the existing "<state>"

    Examples: 
      | address      | state |
      | 111 Ham Lane | VA    |
      | 222 Ham Lane | CA    |

  Scenario Outline: Reference level validation fails on invalid references
    Given the "<address>" has a remote reference to "<state>"
    When the reference level validation is performed on the value "<address>"
    Then the reference level validation returns an error for the non-existant "<state>"

    Examples: 
      | address      | state |
      | 111 Ham Lane | ZZ    |
      | 222 Ham Lane | XY    |

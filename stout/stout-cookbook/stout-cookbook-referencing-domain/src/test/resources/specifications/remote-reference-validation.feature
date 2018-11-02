@remoteReferenceValidation
Feature: Reference Level Validation Requirements

  #tests for remote reference
  Scenario Outline: Reference level validation checks that a referenced value exists
    Given the objects "e1" and "e2" exist
    And "<entityOne>" has a remote reference to "<entityTwo>"
    When the reference level validation is performed on the value "<entityOne>"
    Then the reference level validation returns an error for the referenced value that does not exist

    Examples: 
      | entityOne | entityTwo | 
      | e1        | e2        | 
      | e1        | e3        | 

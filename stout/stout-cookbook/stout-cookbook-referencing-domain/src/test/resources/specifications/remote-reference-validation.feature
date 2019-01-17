@remoteReferenceValidation
Feature: Reference Level Validation Requirements
  
  Reference level validation checks that a referenced value exists

  #tests for remote reference
  Scenario Outline: Reference level validation passes on valid references
    Given the "<address>" has a remote reference to an existing "<state>"
    When the reference level validation is performed on the instance "<address>"
    Then the reference level validation passes

    Examples: 
      | address       | state |
      | 111 Good Lane | VA    |
      | 222 Good Lane | CA    |

  Scenario Outline: Reference level validation fails on invalid references
    Given the "<address>" has a remote reference to a non-existing "<state>"
    When the reference level validation is performed on the instance "<address>"
    Then the reference level validation fails

    Examples: 
      | address      | state |
      | 111 Bad Lane | ZZ    |
      | 222 Bad Lane | XY    |


Scenario Outline: Non-persistent entity can have an external reference
    Given a transient entity "<schoolBuilding>"
    And an entity "<county>"  in another domain
    When references are added
    Then the transient entity "<schoolBuilding>" can have a reference  "<county>"
    
    Examples: 
      | schoolBuilding | county     |
      | schoolABC      | Alexandria |
      | schoolDEF      | Annandale  |         

  Scenario Outline: Non-persistent entity reference level validation passes on valid references
    Given a transient entity "<schoolBuilding>" has a remote reference to an existing entity "<county>"
    When the reference level validation is performed on the instance "<schoolBuilding>"
    Then the reference level validation passes

    Examples: 
      | schoolBuilding | county     |
      | schoolABC      | Alexandria |
      | schoolDEF      | Annandale  | 

  Scenario Outline: Non-persistent entity reference level validation fails on invalid references
    Given a transient entity "<schoolBuilding>" has a remote reference to a non-existing entity "<county>"
    When the reference level validation is performed on the instance "<schoolBuilding>"
    Then the reference level validation fails

    Examples: 
      | schoolBuilding | county     |
      | schoolABC      | Alexandria |
      | schoolDEF      | Annandale  | 
      
      
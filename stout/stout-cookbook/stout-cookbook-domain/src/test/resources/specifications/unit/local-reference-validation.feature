@localReferenceValidation
Feature: Reference Level Validation Requirements
  
  Reference level validation checks that a referenced value exists

  #tests for local reference
  Scenario Outline: Persistent entity reference level validation passes on valid persistent references
    Given the "<address>" has a local reference to an existing "<state>"
    When the reference level validation is performed on the value "<address>"
    Then the reference level validation passes

    Examples: 
      | address       | state |
      | 111 Good Lane | VA    |
      | 222 Good Lane | CA    |

  Scenario Outline: Persistent entity reference level validation fails on invalid persistent references
    Given the "<address>" has a local reference to a non-existing "<state>"
    When the reference level validation is performed on the value "<address>"
    Then the reference level validation fails

    Examples: 
      | address      | state |
      | 111 Bad Lane | ZZ    |
      | 222 Bad Lane | XY    |

  Scenario Outline: Persistent entity reference level validation passes on valid transient entity references
    Given a persistent entity "<building>" has a local reference to an existing transient entity "<postalDistrict>"
    When the reference level validation is performed on the persistent entity instance "<building>"
    Then the reference level validation on persistent entity passes

   Examples: 
      | building  | postalDistrict |
      | schoolABC | PS1            |
      | schoolDEF | PS2            |   
      
  Scenario Outline: Persistent entity reference level validation fails on invalid transient entity references
    Given a persistent entity "<building>" has a local reference to a non-existing transient entity "<postalDistrict>"
    When the reference level validation is performed on the persistent entity instance "<building>"
    Then the reference level validation on persistent entity fails

    Examples: 
      | building  | postalDistrict |
      | schoolJKL | PS99           |
      | schoolMNO | PS100          |   
      
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
      
  Scenario Outline: Transient entity can have an external reference to a persistent entity
    Given a "transient" entity "<schoolBuilding>"
    And a "persistent" entity "<county>" in another domain
    When references are added
    Then the "transient" entity "<schoolBuilding>" can have a reference "<county>"
    
    Examples: 
      | schoolBuilding | county     |
      | schoolABC      | Alexandria |
      | schoolDEF      | Annandale  | 
  
  Scenario Outline: Transient entity can have an external reference to a transient entity
    Given a "transient" entity "<schoolBuilding>"
    And a "transient" entity "<postalDistrict>" in another domain
    When references are added
    Then the "transient" entity "<schoolBuilding>" can have a reference "<postalDistrict>"
    
    Examples: 
      | schoolBuilding | postalDistrict |
      | schoolABC      | PS1            |
      | schoolDEF      | PS2            |   
    
  Scenario Outline: Persistent entity can have an external reference to a transient entity
    Given a "persistent" entity "<building>"
    And a "transient" entity "<postalDistrict>" in another domain
    When references are added
    Then the "persistent" entity "<building>" can have a reference "<postalDistrict>"
    
    Examples: 
      | building | postalDistrict |
      | BL1      | PS1            |
      | BL2      | PS2            |     
  
    Scenario Outline: Transient entity reference level validation passes on valid persistent entity references
    Given a "transient" entity "<schoolBuilding>" has a remote reference to an existing persistent entity "<county>"
    When the reference level validation is performed on the "transient" entity instance "<schoolBuilding>"
    Then the reference level validation on transient entity passes

    Examples: 
      | schoolBuilding | county     |
      | schoolABC      | Alexandria |
      | schoolDEF      | Annandale  | 
      
  Scenario Outline: Transient entity reference level validation fails on invalid peristent entity references
    Given a "transient" entity "<schoolBuilding>" has a remote reference to a non-existing persistent entity "<county>"
    When the reference level validation is performed on the "transient" entity instance "<schoolBuilding>"
    Then the reference level validation on transient entity fails

    Examples: 
      | schoolBuilding | county       |
      | schoolABC      | Loudon       |
      | schoolDEF      | Spotsylvania | 
               
  Scenario Outline: Transient entity reference level validation passes on valid transient entity references
    Given a "transient" entity "<schoolBuilding>" has a remote reference to an existing transient entity "<postalDistrict>"
    When the reference level validation is performed on the "transient" entity instance "<schoolBuilding>"
    Then the reference level validation on transient entity passes

    Examples: 
      | schoolBuilding | postalDistrict |
      | schoolABC      | PS1            |
      | schoolDEF      | PS2            |   
  
  Scenario: Transient entity reference level validation fails on invalid transient entity references
    Given a "transient" entity "schoolBuilding" has a remote reference to a non-existing transient entity
    When the reference level validation is performed on the "transient" entity instance "schoolBuilding"
    Then the reference level validation on transient entity fails

  Scenario Outline: Persistent entity reference level validation passes on valid transient entity references
    Given a "persistent" entity "<building>" has a remote reference to an existing transient entity "<postalDistrict>"
    When the reference level validation is performed on the "persistent" entity instance "<building>"
    Then the reference level validation on persistent entity passes

    Examples: 
      | building  | postalDistrict |
      | schoolABC | PS1            |
      | schoolDEF | PS2            |    
    
  Scenario: Persistent entity reference level validation fails on invalid transient entity references
    Given a "persistent" entity "Building" has a remote reference to a non-existing transient entity
    When the reference level validation is performed on the "persistent" entity instance "Building"
    Then the reference level validation on persistent entity fails       




      
      
      
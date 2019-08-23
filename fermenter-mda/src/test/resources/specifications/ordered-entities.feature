@orderedEntities
Feature: Entities -> Ordered
  I want to be able inquire as to the order on references and relations to allow precedence of entity-related operations

  Scenario: Entities without references or relations are ordered alphabetically
    Given the following entities:
      | entityName |
      | Gamma      |
      | Alpha      |
      | Beta       |
    When the entities are loaded
    Then the values are listed in the following order:
      | Alpha |
      | Beta  |
      | Gamma |

  Scenario: References are precursors of the current entity
    Given the following entities and their references:
      | entityName | references |
      | Country    |            |
      | State      | Country    |
      | City       | State      |
    When the entities are loaded
    Then "Country" is a precursor of "State"
    And "State" is a precursor of "City"

  Scenario: Multiple references are ordered as precursors
    Given the following entities and their references:
      | entityName | references |
      | Blah       | Foo, Bar   |
      | Foo        |            |
      | Bar        |            |
    When the entities are loaded
    Then "Foo" is a precursor of "Blah"
    And "Bar" is a precursor of "Blah"

  Scenario: Relations are children of the current entity
    Given the following entities and their relations:
      | entityName | relations           |
      | Apartment  |                     |
      | Elevator   |                     |
      | Building   | Elevator, Apartment |
    When the entities are loaded
    Then "Building" is a precursor of "Elevator"
    And "Building" is a precursor of "Apartment"

  Scenario: Intermixed values to ensure referenced values are first and alphabetic when otherwise of equal weight
    Given the following entities:
      | entityName | references |
      | Blah       | Foo, Bar   |
      | Foo        |            |
      | Bar        |            |
      | Alpha      |            |
      | Beta       |            |
      | Gamma      | Bar        |
    When the entities are loaded
    Then the values are listed in the following order:
      | Bar   |
      | Foo   |
      | Alpha |
      | Beta  |
      | Blah  |
      | Gamma |

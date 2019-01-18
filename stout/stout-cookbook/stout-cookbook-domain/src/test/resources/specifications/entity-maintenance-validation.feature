@entityValidation
Feature: Entity Level Validation

  Have findByPrimaryKey handle null keys without throwing NPE

  Scenario Outline: An entity can be found by their id
    Given an entity exists in the system with the id <id>
    When the system attempts to grab the entity by the id <id>
    Then the entity with the id <id> is returned

    Examples: 
      | id |
      | 1  |
      | 2  |
      | 3  |

  Scenario Outline: An error is thrown if an entity cannot be found by their id
    Given an entity exists in the system with the id <idOne>
    When the system attempts to grab the entity by the id <idTwo>
    Then an error message and HTTP Status 400 is thrown

    Examples: 
      | idOne | idTwo |
      | 1     | 17    |
      | 2     | 200   |
      | 3     | ab    |

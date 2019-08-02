@entityValidation
Feature: Entity Level Validation

  Have findByPrimaryKey handle null keys without throwing NPE

  Scenario Outline: An entity can be found by their id
    Given an entity exists in the system with the id "<id>"
    When the system attempts to grab the entity by the id "<id>"
    Then the entity with the id "<id>" is returned without any errors

    Examples:
      | id |
      | 1  |
      | 2  |
      | 3  |

  Scenario Outline: An error is thrown if no entity can be found by the id
    Given an entity exists in the system with the id "<idOne>"
    When the system attempts to grab the entity by the id "<idTwo>"
    Then an error message and HTTP Status 400 is thrown

    Examples:
      | idOne | idTwo |
      | 4     | 17    |
      | 5     | 200   |
      | 6     | ab    |

  Scenario: An error is thrown if a null id is used to look up an entity
    When the system attempts to grab an entity by a null id
    Then an error message and HTTP Status 400 is thrown

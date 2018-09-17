@fieldValidation
Feature: Field Level Validation Requirements

  Scenario Outline: maxLength validation against valid values (ex: 20 character max)
    Given a "<value>" to validate against the validation example string example field
    When field level validation is performed on that value
    Then the validation returns no errors

    Examples: 
      | value                |
      | ab                   |
      | foo                  |
      | bdd is great         |
      | abcdefghijklmnopqrs  |
      | abcdefghijklmnopqrst |

  Scenario Outline: maxLength validation against invalid values (ex. exceeds 20 character max)
    Given a "<value>" to validate against the validation example string example field
    When field level validation is performed on that value
    Then the validation return errors

    Examples: 
      | this will produce an error                              |
      | abcdefghijklmnopqrstu                                   |
      | aaaaaaabbbbbbbbbbcccccccccc ddddddddddeeeeeeeefffffffff |

  Scenario Outline: minLength validation against valid values (ex: 2 character min)
    Given a "<value>" to validate against the validation example string example field
    When field level validation is performed on that value
    Then the validation returns no errors

    Examples: 
      | values               |
      | at                   |
      | foo                  |
      | bdd is great         |
      | abcdefghijklmnopqrs  |
      | abcdefghijklmnopqrst |

  Scenario Outline: minLength validation against invalid values (ex. does not meet 2 character min)
    Given a "<value>" to validate against the validation example string example field
    When field level validation is performed on that value
    Then the validation return errors

    Examples: 
      | a |
      | b |
      |   |

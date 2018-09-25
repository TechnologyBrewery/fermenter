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
    Then the validation returns errors

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
    Then the validation returns errors

    Examples: 
      | a |
      | b |
      |   |

  Scenario Outline: maxValue validation against valid values (ex. positive Long.MAX_VALUE)
    Given a <long> to validate against the validation example long example field
    When field level validation is performed on that long value
    Then the long validation returns no errors

    Examples: 
      | long      |
      | 100000000 |
      |   6637985 |
      | 123456789 |

  Scenario Outline: maxValue validation against invalid values (ex. exceeds Long.MAX_VALUE)
    Given a <long> to validate against the validation example long example field
    When field level validation is performed on that long value
    Then the long validation returns errors

    Examples: 
      | long      |
      | 999999999 |
      | 123456790 |

  Scenario Outline: minValue validation against valid values (ex. negative Long.MAX_VALUE)
    Given a negative <long> to validate against the validation example long example field
    When field level validation is performed on that long value
    Then the long validation returns no errors

    Examples: 
      | long       |
      | -100000000 |
      |   -6637985 |
      | -123456789 |

  Scenario Outline: minValue validation against invalid values (ex. exceeds negative Long.MAX_VALUE)
    Given a negative <long> to validate against the validation example long example field
    When field level validation is performed on that long value
    Then the long validation returns errors

    Examples: 
      | long       |
      | -999999999 |
      | -123456790 |
      
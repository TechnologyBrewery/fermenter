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

  Scenario Outline: maxValue validation against valid values (ex. positive Long maxValue)
    Given a <long> to validate against the validation example long example field
    When field level validation is performed on that long value
    Then the long validation returns no errors

    Examples: 
      | long      |
      | 100000000 |
      |   6637985 |
      | 123456789 |

  Scenario Outline: maxValue validation against invalid values (ex. exceeds Long maxValue)
    Given a <long> to validate against the validation example long example field
    When field level validation is performed on that long value
    Then the long validation returns errors

    Examples: 
      | long      |
      | 999999999 |
      | 123456790 |

  Scenario Outline: minValue validation against valid values (ex. negative Long minValue)
    Given a negative <long> to validate against the validation example long example field
    When field level validation is performed on that long value
    Then the long validation returns no errors

    Examples: 
      | long       |
      | -100000000 |
      |   -6637985 |
      | -123456789 |

  Scenario Outline: minValue validation against invalid values (ex. exceeds negative Long minValue)
    Given a negative <long> to validate against the validation example long example field
    When field level validation is performed on that long value
    Then the long validation returns errors

    Examples: 
      | long       |
      | -999999999 |
      | -123456790 |

  Scenario Outline: maxValue validation against valid values (ex. positive Integer maxValue in entity)
    Given an <integer> to validate against the validation example integer example field
    When field level validation is performed on that integer value
    Then the integer validation returns no errors

    Examples: 
      | integer |
      |   12345 |
      |     999 |
      |       1 |

  Scenario Outline: maxValue validation against invalid values (ex. exceeds positive Integer maxValue in entity)
    Given an <integer> to validate against the validation example integer example field
    When field level validation is performed on that integer value
    Then the integer validation returns errors

    Examples: 
      | integer   |
      |     12346 |
      |   9999999 |
      | 100000000 |

  Scenario Outline: minValue validation against valid values (ex. negative Integer minValue in entity)
    Given a negative <integer> to validate against the validation example integer example field
    When field level validation is performed on that integer value
    Then the integer validation returns no errors

    Examples: 
      | integer |
      |  -12345 |
      |    -999 |
      |      -1 |

  Scenario Outline: maxValue validation against invalid values (ex. exceeds negative Integer minValue in entity)
    Given a negative <integer> to validate against the validation example integer example field
    When field level validation is performed on that integer value
    Then the integer validation returns errors

    Examples: 
      | integer    |
      |     -12346 |
      |   -9999999 |
      | -100000000 |

  Scenario Outline: maxValue validation against valid values (ex. positive BigDecimal maxValue)
    Given a <bigdecimal> to validate against the validation example BigDecimal example field
    When field level validation is performed on that BigDecimal value
    Then the BigDecimal validation returns no errors

    Examples: 
      | bigdecimal          |
      | 123456789.123456789 |
      |          12.3456789 |
      |  99999999.999999999 |

  Scenario Outline: maxValue validation against invalid values (ex. exceeds positive BigDecimal maxValue)
    Given a <bigdecimal> to validate against the validation example BigDecimal example field
    When field level validation is performed on that BigDecimal value
    Then the BigDecimal validation returns errors

    Examples: 
      | bigdecimal            |
      |   123456790.123456790 |
      | 1234567891.3456789121 |
      |  99999999199.99999999 |

  Scenario Outline: minValue validation against valid values (ex. negative BigDecimal minValue)
    Given a negative <bigdecimal> to validate against the validation example BigDecimal example field
    When field level validation is performed on that BigDecimal value
    Then the BigDecimal validation returns no errors

    Examples: 
      | bigdecimal           |
      | -123456789.123456789 |
      |          -12.3456789 |
      |  -99999999.999999999 |

  Scenario Outline: minValue validation against invalid values (ex. exceeds negative BigDecimal minValue)
    Given a negative <bigdecimal> to validate against the validation example BigDecimal example field
    When field level validation is performed on that BigDecimal value
    Then the BigDecimal validation returns errors

    Examples: 
      | bigdecimal             |
      |   -123456790.123456790 |
      | -1234567891.3456789121 |
      |   -9999999999.99999999 |

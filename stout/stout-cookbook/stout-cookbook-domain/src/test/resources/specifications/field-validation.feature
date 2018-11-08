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

  Scenario Outline: RegEx String format validation against format
    Given a "<value>" to validate against the regEx example String example field
    When field level validation is performed on that regEx String value
    Then the regEx String validation returns no errors

    Examples: 
      | value                                  |
      | example@exampleemail.com               |
      | theexample4validaiton@validaddress.org |
      | echun@cpointe-inc.com                  |

  Scenario Outline: RegEx String format validation against incorrect format
    Given a "<value>" to validate against the regEx example String example field
    When field level validation is performed on that regEx String value
    Then the regEx String validation returns errors

    Examples: 
      | value                         |
      | example@exampleemail          |
      | @validaddress.org             |
      | echuncpointe-inc.com          |
      | someperson1234@2346836777123. |
      
  Scenario Outline: RegEx Zipcode format validation against valid format
    Given a "<value>" to validate against the regEx example Zipcode example field
    When field level validation is performed on that regEx Zipcode value
    Then the regEx Zipcode validation returns no errors

    Examples: 
      | value      |
      | 12345      |
      | 12345-6789 |
     
  Scenario Outline: RegEx Zipcode format validation against incorrect format
	  Given a "<value>" to validate against the regEx example Zipcode example field
    When field level validation is performed on that regEx Zipcode value
    Then the regEx Zipcode validation returns errors

    Examples: 
      | value        |
      | 1            |
      | 12345-67894  |
      | testExample  |
      |              |

  Scenario Outline: required field validation that contains a value
    Given a "<value>" to validate against the required field String example field
    When a field validation is performed on the required field String value
    Then the required field returns a value with no errors

    Examples: 
      | value            |
      | this is required |
      | String value     |

  Scenario: required field validation that contains no value
    Given a null to validate against the required field String example field
    When a field validation is performed on the required field String value
    Then the required field returns a null with errors

  Scenario Outline: a child required field validation that contains a value
    Given a "<value>" to validate against the child required field String example field
    When a field validation is performed on the child required field String value
    Then the child required field returns a value with no errors

    Examples: 
      | value                    |
      | this is a child required |
      | String value for child   |

  Scenario: a child required field validation that contains no value
    Given a null to validate against the child required field String example field
    When a field validation is performed on the child required field String value
    Then the child required field returns a null with errors

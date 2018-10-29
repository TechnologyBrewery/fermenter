@enumeration
Feature: Specify enumerations for use in model-driven file generation

  Scenario Outline: specify named enumerations via a JSON metamodel
    Given an enumeration named "<name>" in "<package>" and enum constants "<constants>"
    When enumerations are read
    Then an enumeration metamodel instance is returned for the name "<name>" in "<package>" with the enum constants "<constants>"
    And the enumeration is of type "named"

    Examples: 
      | name      | package       | constants                                                      |
      | Compass   | my.navigation | North, East, South, West                                       |
      | DayOfWeek | my.calendar   | Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday |

  Scenario Outline: Namespace is used to incorrectly find enumerations
    Given an enumeration named "<name>" in "<package>" and enum constants "<constants>"
    When enumerations are read
    Then NO enumeration metamodel instance is returned for the name "<name>" in "<lookupPackage>"

    Examples: 
      | name        | package       | constants                | lookupPackage |
      | Compass     | my.navigation | North, East, South, West | alt.package   |
      | WeekendDays | my.weekends   | Saturday, Sunday         | my.weekdays   |

  Scenario Outline: Single valued enumerations are supported
    Given an enumeration named "<name>" in "<package>" and enum constant names "<constantNames>" and values "<constantValues>"
    When enumerations are read
    Then an enumeration metamodel instance is returned for the name "<name>" in "<package>" with the enum constants "<constantNames>" and matching values "<constantValues>"
    And the enumeration is of type "valued"

    Examples: 
      | name          | package     | constantNames            | constantValues |
      | FirstQuarter  | my.calendar | January, February, March | 1, 2, 3        |
      | SecondQuarter | my.calendar | April, May, June         | 4, 5, 6        |

  Scenario Outline: when enumeration name does not match file name an error is returned
    Given an enumeration named "<name>" in "<fileName>"
    When enumerations are read
    Then an error is returned

    Examples: 
      | name      | fileName          |
      | Compass   | notCompass.json   |
      | DayOfWeek | notDayOfWeek.json |

  Scenario: FUTURE - use an enumeration as field type in an entity metamodel
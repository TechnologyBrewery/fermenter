@enumeration
Feature: Specify enumerations for use in model-driven file generation

  Scenario Outline: specify named enumerations via a JSON metamodel
    Given an enumeration named "<name>" in "<namespace>" and enum constants "<constants>"
    When enumerations a read
    Then an enumeration metamodel instance is returned for the name "<name>" in "<namespace>" with the enum constants "<constants>"

    Examples: 
      | name      | package       | constants                                                      |
      | Compass   | my.navigation | North, East, South, West                                       |
      | DayOfWeek | my.calendar   | Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday |

  Scenario Outline: Namespace is used to incorrectly find enumerations
    Given an enumeration named "<name>" in "<namespace>" and enum constants "<constants>"
    When enumerations a read
    Then NO enumeration metamodel instance is returned for the name "<name>" in "<namespace>"

    Examples: 
      | name    | package       | constants                |
      | Compass | my.navigation | North, East, South, West |

  Scenario: FUTURE - Single valued enumerations are supported

  Scenario: FUTURE - Multi-valued enumerations are supported

  Scenario: FUTURE - use an enumeration as field type in an entity metamodel

  Scenario: FUTURE - use an enumeration as a parameter in an serivce metamodel

  Scenario: FUTURE - use an enumeration as a return type in an serivce metamodel

@messageGroup
Feature: Specify mesasge groups for use in model-driven file generation

  Scenario Outline: a group can be created with one or more messages
    Given a message group named "<name>" in "<package>" and the messages:
      | name          | text           |
      | <messageKey1> | <messageText1> |
      | <messageKey2> | <messageText2> |
    When message groups are read
    Then a meessage group is returned for the name "<name>" in "<package>" and the messages:
      | name          | text           |
      | <messageKey1> | <messageText1> |
      | <messageKey2> | <messageText2> |

    Examples: 
      | name         | package         | messageKey1   | messageText1           | messageKey2        | messageText2       |
      | launchGroup  | rocket.ignition | launch.error  | Failure to launch!     | launch.success     | Launch successful! |
      | requiredness | core            | missing.value | Required value missing | value.format.error | Cannot read value! |

  Scenario: required field name is not provided
    Given a message group named "" in "missing.name.package" and a least one valid message
    When message groups are read
    Then the generator throws an exception about a invalid mssage group

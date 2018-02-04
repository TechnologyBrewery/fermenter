Feature: Track messages during metadata load to allow bulk reporting to maximize the number of issues that are simultaneously reported

  Scenario Outline: collect and return errors
    Given multiple error messages "<errorMessages>"
    When the message tracker is asked for messages
    Then the tracker reports that errors were encountered

    Examples: 
      | errorMessages              |
      | bad profile!               |
      | bad syntax, missing target |
      | one, two, three            |

  Scenario Outline: collect and return warnings
    Given multiple warning messages "<warningMessages>"
    When the message tracker is asked for messages
    Then the tracker reports that no errors were encountered

    Examples: 
      | warningMessages    |
      | careful about that |
      | warning, warning2  |
      | one, two, three    |

  Scenario Outline: collect and return errors and warnings
    Given multiple error messages "<errorMessages>"
    And multiple warning messages "<warningMessages>"
    When the message tracker is asked for messages
    Then the tracker reports that errors were encountered

    Examples: 
      | errorMessages              | warningMessages               |
      | bad profile!               | some warning                  |
      | bad syntax, missing target | watch out for this            |
      | bad profile!               | some warning, another warning |
      | bad syntax, missing target | some warning, another warning |

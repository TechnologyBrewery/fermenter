@dateParameterClient
Feature: Date Parameter

  Use date parameter without getting 400 error

  Scenario Outline: Ensure date parameters are translated successfully
    Given a simple domain with today's date
    When the simple domain for today's date is retrieved using "<dateType>"
    Then the simple domain is retrieved successfully

    Examples: 
      | dateType       |
      | java.util.Date |
      | java.sql.Date  |

  Scenario:  Null date does not result in 400 error
    Given a simple domain with today's date
    When the simple domain for null date is retrieved 
    Then there are no errors
    But no simple domains are returned

@dateParameterClient
Feature: Date Parameter
  Use date parameter without getting 400 error

  Scenario Outline: Ensure date parameters are translated successfully
    Given a simple domain entity with today's date
    When the simple domain for today's date is retrieved using "<dateType>"
    Then the entity is retrieved
    And there are no errors 

    Examples: 
      | dateType                | 
      | java.util.Date          | 
      | java.sql.Date           | 
      | hibernate.type.DateType |


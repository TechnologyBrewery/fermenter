@dateParameterClient
Feature: Date Parameter
  Use date parameter without getting 400 error

  Scenario Outline: Ensure date parameters are translated successfully
    Given a simple domain entity with today's date
    And the service implementation method has a parameter type "<dateType>"
    When the service retrieves the simple domain for today's date
    Then the entity is retrieved
    And there are no errors 

    Examples: 
      | dateType                | 
      | java.util.Date          | 
      | java.sql.Date           | 
      | hibernate.type.DateType |


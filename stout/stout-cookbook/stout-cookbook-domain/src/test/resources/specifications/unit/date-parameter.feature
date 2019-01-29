@dateParameter
Feature: Date Parameter
  Retrieve correct records when method signature has a date parameter

  Scenario: Retrieve correct records for prior to today's date
    Given there are "4" records prior to today's date
    And "1" record for today
    And "2" records for tomorrow
    When I want to get all records prior to today's date
    Then I should get "4" records
    
  Scenario: Retrieve correct records for today
    Given there are "4" records prior to today's date
    And "1" record for today
    And "2" records for tomorrow
    When I want to get all records for today
    Then I should get "1" records
    




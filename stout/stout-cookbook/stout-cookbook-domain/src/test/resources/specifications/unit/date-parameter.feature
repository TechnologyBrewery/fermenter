@dateParameter
Feature: Date Parameter
  Retrieve correct records when method signature has a date parameter

  Scenario: Retrieve correct records for prior to today's date
    Given there are 4 simple domain objects with dates prior to today's date
    And 1 simple domain object with today's date
    And 2 simple domain objects with tomorrow's date
    When I want to get all simple domain objects prior to today's date
    Then I should get 4 simple domain objects
    @jaana
  Scenario: Retrieve correct records for today
    Given there are 4 simple domain objects with dates prior to today's date
    And 1 simple domain object with today's date
    And 2 simple domain objects with tomorrow's date
    When I want to get all simple domain objects for today
    Then I should get 1 simple domain objects
    




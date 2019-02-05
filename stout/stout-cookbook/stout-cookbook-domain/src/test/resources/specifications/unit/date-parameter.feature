@dateParameter
Feature: Filter by date
  
  Filter simple domains by date

  Scenario Outline: Retrieve simple domains by date
    Given there are simple domains with dates in the past, present, and future
    When I want to get all simple domains "<comparison>" today
    Then I should get only simple domains "<comparison>" today

    Examples: 
      | comparison |
      | prior to   |
      | for        |
      | after      |

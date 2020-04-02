@findByExampleDefaults
Feature: Find by example with defaults
  
  Ensure that entities can be found by example using default paging and sorting

  Scenario: Find by example without a sort will sort by ascending identifier by default
    Given entities that have something in common
    When a search is done using an example of whatever is in common
    Then the results will be sorted by ascending primary key by default

  Scenario: Find by example without paging info will return the first page by default
    Given entities that have something in common
    When a search is done using an example of whatever is in common
    Then the search will start on page 0 by default

  Scenario: Find by example without paging info limits the maximum number of results returned by default
    Given a large number of entities that have something in common
    When a search is done using an example of whatever is in common
    Then the search will limit the number of search results by default

  Scenario: Find by example with no example defined uses find all by default
    Given some number of entities exist
    When a search is done without specifying an example
    Then the search will do a find all by default

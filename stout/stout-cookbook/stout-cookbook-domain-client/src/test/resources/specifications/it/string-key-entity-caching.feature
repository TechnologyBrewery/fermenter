@stringKeyedEntityCaching
Feature: Caching -> String keyed reference caching in the maintenance rest client

  Scenario: I can use a reference key with blank spaces in the cache
    Given a referenced entity with a string key with blank spaces exists in the foreign domain
    And I look up the string keyed reference with the maintenance rest client
    When I look up the string keyed reference again with the maintenance rest client
    Then the cached string keyed referenced is used

  Scenario Outline: I can lookup a reference with a key with blank spaces in the cache
    Given a referenced entity with a string key exists in the foreign domain
    And I look up the string keyed reference with the maintenance rest client
    When I look up the same reference again with a key with <numberOfSpacesInLookupKey> with the maintenance rest client
    Then the cached string keyed referenced is used

    Examples: 
      | numberOfSpacesInLookupKey |
      |                         0 |
      |                         2 |
      |                        10 |
      |                        40 |

@referenceCaching
Feature: Caching -> Reference Caching in Maintence rest client

  Scenario: Reference entities are cached when using the maintenance rest client
    Given a referenced entity exists in the foreign domain
    When I look up a reference with the maintenance rest client
    Then the reference is cached for later calls

  Scenario: Cached references are used on subsequent lookups using the maintance rest client
    Given a referenced entity exists in the foreign domain
    And I look up a reference with the maintenance rest client
    When I look up the same reference again with the maintenance rest client
    Then the cached referenced is used

  Scenario: Cached references are removed when deleted through the maintance rest client
    Given a referenced entity exists in the foreign domain
    When I delete an existing entity through the maintenance rest client
    Then the cached entity is removed

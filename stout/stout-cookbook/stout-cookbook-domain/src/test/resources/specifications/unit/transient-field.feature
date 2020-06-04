@transientField
Feature: Transient Field Requirements

  Scenario Outline: A field that is marked as transient should not persist in the database
    Given a field is marked as transient and I assign "<value>" to the field value
    When the entity is saved and retrieved
    Then the only fields with values are the ones not marked as transient

    Examples: 
      | value                |
      | ab                   |
      | foo                  |
      | bdd is great         |
      | abcdefghijklmnopqrs  |
      | abcdefghijklmnopqrst |

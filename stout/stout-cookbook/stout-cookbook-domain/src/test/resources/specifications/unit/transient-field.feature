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
      
  Scenario: Default value of transient field on persistent entity
  	Given a persistent entity has a transient field with a default value
  	When the entity is retrieved
  	Then the value of the field is equal to the default
  	
  Scenario: The set value is returned for transient field on persisted entity
  	Given a persistent entity has a transient field with a default value
  	And the value of the field has been modified
  	When the entity is retrieved
  	Then the field has the new value
  	
  Scenario: Default value of field on transient object
  	Given a transient entity has a field with a default value
  	When the entity is retrieved
  	Then the value of the field is equal to the default
  	
  Scenario: The set value is returned for a field on a transient entity
  	Given a transient entity has a field with a default value
  	And the value of the field has been modified
  	When the entity is retrieved
  	Then the field has the new value

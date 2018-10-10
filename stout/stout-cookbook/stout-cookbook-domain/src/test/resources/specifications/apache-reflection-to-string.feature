
@apacheReflectionToString
Feature: Reflection to String with Apache

Background:
    Given the following object types exist with the following field names
        | objectType               | fieldNames                                  |
        | IdentityKeyedEntityBO    | repository:id:internalTransientID:validator |
        | TransientEntityExampleBO | name:requiredReference:validator            |

Scenario: Check field names of IdentityKeyedEntityBO reflection string
    Given the object type is "IdentityKeyedEntityBO"
    When the system returns the reflection string
    Then the reflection string should contain all field names

Scenario: Check field names of TransientEntityExampleBO reflection string
    Given the object type is "TransientEntityExampleBO"
    When the system returns the reflection string
    Then the reflection string should contain all field names






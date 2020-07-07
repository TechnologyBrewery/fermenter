@message
Feature: Support informational and error messages to communicate errors across architecture layers

  Scenario: Ensure that the same message used to report two identical errors ARE equal
    Given the following messages:
      | messageName   | insertName | insertValue |
      | invalid.field | fieldName  |        1000 |
      | invalid.field | fieldName  |        1000 |
    When the messages are compared for equality
    Then they are equal

  Scenario: Ensure that the same message used to report two different errors ARE NOT equal
    Given the following messages:
      | messageName   | insertName | insertValue |
      | invalid.field | fieldName  |        1111 |
      | invalid.field | fieldName  |        2222 |
    When the messages are compared for equality
    Then they are not equal

  Scenario: Ensure the same message with different insert names ARE NOT equal (shouldn't happen, but for completeness)
    Given the following messages:
      | messageName   | insertName | insertValue |
      | invalid.field | fieldName  |        1111 |
      | invalid.field | foobar     |        1111 |
    When the messages are compared for equality
    Then they are not equal

  Scenario: Ensure that different messages ARE NOT equal
    Given the following messages:
      | messageName   | insertName | insertValue |
      | invalid.field | fieldName  |        1111 |
      | missing.field | fieldName  |        1111 |
    When the messages are compared for equality
    Then they are not equal

  Scenario: Ensure that the same message with different severities ARE NOT equals
    Given the following messages:
      | messageName  | severity |
      | some.message | ERROR    |
      | some.message | INFO     |
    When the messages are compared for equality
    Then they are not equal

  Scenario: Ensure that the same message used to report two different errors do not have a hash collision
    Given the following messages:
      | messageName   | insertName | insertValue |
      | invalid.field | fieldName  |        1000 |
      | invalid.field | fieldName  |        1000 |
    When the messages hash codes are compared
    Then they have identical hash codes

  Scenario: Ensure that the same message used to report two different errors ARE NOT equal
    Given the following messages:
      | messageName   | insertName | insertValue |
      | invalid.field | fieldName  |        1111 |
      | invalid.field | fieldName  |        2222 |
    When the messages hash codes are compared
    Then they have different hash codes

  Scenario: Ensure the same message with different insert names ARE NOT equal (shouldn't happen, but for completeness)
    Given the following messages:
      | messageName   | insertName | insertValue |
      | invalid.field | fieldName  |        1111 |
      | invalid.field | foobar     |        1111 |
    When the messages hash codes are compared
    Then they have different hash codes

  Scenario: Ensure that different messages ARE NOT equal
    Given the following messages:
      | messageName   | insertName | insertValue |
      | invalid.field | fieldName  |        1111 |
      | missing.field | fieldName  |        1111 |
    When the messages hash codes are compared
    Then they have different hash codes

  Scenario: Ensure that the same message with different severities ARE NOT equal
    Given the following messages:
      | messageName  | severity |
      | some.message | ERROR    |
      | some.message | INFO     |
    When the messages hash codes are compared
    Then they have different hash codes

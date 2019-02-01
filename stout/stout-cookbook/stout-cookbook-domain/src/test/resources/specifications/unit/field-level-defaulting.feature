@fieldLevelDefaulting
Feature: Allow fields to be defaulted through the metamodel

  Scenario: Test that the default values on a DefaultValueExample object hold the correct values through the metamodel
    Given a DefaultValueExample object
    When the user checks the field value for every <fieldName>
    Then the following <fieldValue> is returned
      | fieldName     | fieldValue          |
      | Name          | Default name value  |
      | Type          | DefaultType         |
      | Long          |            31415926 |
      | Decimal       |         3.141592645 |
      | Boolean       | false               |
      | ConfusingJson | true                |
      | Integer       |                   1 |
      | Timestamp     | 2022-07-16 04:26:19 |
      | Date          | 2022-07-16          |

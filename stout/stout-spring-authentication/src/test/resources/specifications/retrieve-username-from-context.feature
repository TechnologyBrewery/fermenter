@username
Feature: Retrieve username from context
  As a developer, I want to retrieve the username sent in by the header from the security context

  Scenario Outline: Retrieve username from context
    Given a username "<username>" is saved as a "<format>"
    When I retrieve the username
    Then I get username "<username>"

    Examples: 
      | username    | format |
      | SMITH       | string |
      | AS28WER098  | string |
      | Smith,Jane  | string |
      | doe, mary   | string |
      | DOE         | User   |
      | 12F98WE     | User   |
      | Doe,Mary    | User   |
      | Smith, Jane | User   |

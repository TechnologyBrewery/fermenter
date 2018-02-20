@TestExceptionHandler
Feature: Create an exception handler and verify that it correctly handles given exceptions properly

  Scenario Outline: Exception handler should catch the original exception and throw a recoverable exception
    When given an exception of type "<exceptionType>" with arguments "<arguments>"
    Then the exception handler should throw a RecoverableException

    Examples:
      | exceptionType                               |    arguments    |
      | java.lang.IndexOutOfBoundsException         |                 |
      | java.lang.NumberFormatException             |                 |
      | java.lang.StringIndexOutOfBoundsException   |                 |

  Scenario Outline: Exception handler should catch the original exception and throw an unrecoverable exception
    When given an exception of type "<exceptionType>" with arguments "<arguments>"
    Then the exception handler should throw an UnrecoverableException

    Examples:
      | exceptionType                               | arguments |
      | java.lang.ClassCastException                |           |
      | java.lang.IllegalAccessException            |           |
      | java.lang.IllegalArgumentException          |           |
      | java.lang.IllegalStateException             |           |
      | java.lang.NoSuchMethodException             |           |
      | java.lang.NullPointerException              |           |
      | java.lang.RuntimeException                  |           |
      | java.lang.UnsupportedOperationException     |           |

    Scenario Outline: Exception handler does not have a case for given exception, so it will throw an unrecoverable exception
      When given an exception of type "<exceptionType>" with arguments "<arguments>"
      Then the exception handler should throw an UnrecoverableException

      Examples:
      | exceptionType                               | arguments |
      | java.lang.IllegalCallerException            |           |
      | java.lang.CloneNotSupportedException        |           |
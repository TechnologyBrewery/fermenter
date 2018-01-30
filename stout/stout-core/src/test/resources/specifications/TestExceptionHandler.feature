Feature: Create an exception handler and verify that it correctly handles given exceptions properly

  Scenario Outline: Exception handler should be given recoverable exceptions
    When given an exception of type "<exceptionType>"
    Then the exception handler should throw a RecoverableException

    Examples:
      | exceptionType                               |
      | java.lang.IllegalAccessException            |
      | java.lang.IndexOutOfBoundsException         |
      | java.lang.NumberFormatException             |
      | java.text.ParseException                    |
      | java.lang.StringIndexOutOfBoundsException   |

  Scenario Outline: Exception handler should be given unrecoverable exceptions
    When given an exception of type "<exceptionType>"
    Then the exception handler should throw an UnrecoverableException

    Examples:
      | exceptionType                               |
      | java.lang.ClassCastException                |
      | java.lang.IllegalArgumentException          |
      | java.lang.IllegalStateException             |
      | java.lang.reflect.InvocationTargetException |
      | java.lang.NoSuchMethodException             |
      | java.lang.NullPointerException              |
      | java.lang.RuntimeException                  |
      | java.lang.UnsupportedOperationException     |

    Scenario Outline: Exception handler does not have a case for given exception
      When given an exception of type "<exceptionType>"
      Then the exception handler should throw an UnrecoverableException

      Examples:
      | exceptionType                               |
      | java.lang.IllegalCallerException            |
      | java.lang.TypeNotPresentException           |
      | java.lang.CloneNotSupportedException        |
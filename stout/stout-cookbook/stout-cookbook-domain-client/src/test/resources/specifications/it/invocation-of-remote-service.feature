@invocationOfRemoteService
Feature: Invocation of Remote Service
  
  Tests the ability invoke a service via the delegate pattern (over REST)

  # Most, if not all, scenarios in this file should also be added to stout-cookbook-local-referencing-domain's local-invocation-of-remote-service.feature
    
  Scenario: test invocation of a method with a void return type
    When a service with a void return type is invoked
    Then a single informational messages is returned indicating successful invocation

  Scenario: test invocation of a method with a standard return type (e.g., string)
    When a service with a standard return type is invoked
    Then a valid standard type is returned

  Scenario: test invocation of a method with an enumeration return type
    When a service with an enumeration return type is invoked
    Then a valid enumeration type is returned

  Scenario: test invocation of a method with an entity return type
    When a service with an entity return type is invoked
    Then a valid entity is returned

  Scenario: test invocation of a method with a collection of standard types as the return type (e.g., multuple strings)
    When a service with a collection of standard types as the return type is invoked
    Then a valid collection of standard types is returned

  Scenario: test invocation of a method with a collection of entities as the return type
    When a service with a collection of entities as the return type is invoked
    Then a valid collection of entities is returned

  Scenario: test invocation of a method with a paged collection of standard types as the return type (e.g., multuple strings)
    When a service with a paged collection of standard types as the return type is invoked
    Then a valid paged collection of standard types is returned

  Scenario: test invocation of a method with a paged collection of entities as the return type
    When a service with a paged collection of entities as the return type is invoked
    Then a valid paged collection of entities is returned

  Scenario: test invocation of a method with no parameters
    When a service with no parameters is invoked
    Then a single informational messages is returned indicating successful invocation

  Scenario: test invocation of a method with a standard type parameter
    When a service with a standard type parameter is invoked
    Then a single informational messages is returned indicating successful invocation

  Scenario: test invocation of a method with an enumeration type parameter
    When a service with an enumeration type parameter is invoked
    Then a single informational messages is returned indicating successful invocation
    
  Scenario: test invocation of a method with an entity parameter
    When a service with an entity parameter is invoked
    Then a single informational messages is returned indicating successful invocation

  Scenario: test invocation of a method with a collection of entities as a parameter
    When a service with a collection of entities as a parameter is invoked
    Then a single informational messages is returned indicating successful invocation

  Scenario: test invocation of a method with both a standard type and entity parameter
    When a service with both a standard type and entity parameter is invoked
    Then a single informational messages is returned indicating successful invocation

  Scenario: test error messages are conveyed across remote calls
    When a service that generates an single error message is invoked
    Then a single error messages is returned indicating a busines-logic troubled invocation
    
	Scenario: test that a flush outside a transaction does not negatively impact restful client
    When a flush is called while outside a transaction
    Then no errors result        

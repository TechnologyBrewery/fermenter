@authentication
Feature: Stout --> Authentication

  Scenario: Authentication information is available within a service
    Given an authenticated user
    When authentication information is requested from within a service
    Then the username is returned

  Scenario: Support programmatic logout from within a service
    Given an authenticated user
    When logout is invoked before authentication information is requested from within a service
    Then no username is returned
    
  Scenario: Logout prevents authorization within a service call
    Given an authenticated user
    When logout is invoked before an operation that requires authorization is invoked
    Then an authorization error is returned   

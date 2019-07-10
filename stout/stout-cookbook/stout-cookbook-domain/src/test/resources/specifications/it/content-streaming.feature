@clientContentStreaming
Feature: Client -> Stream File Content From Server

  Scenario: Retrieve streaming from server
    When I request a file stream from the content respository
    Then I get a file with content

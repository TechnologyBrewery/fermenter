@batchMessageHandling
Feature: Batch Processing -> Message Handling

  Scenario: Control when messages are cleared out during batch processing
    When a batch step that results in an error message is executed 
    And a batch step that result has no errors is executed
    Then the error step should process with an error message returned 
    And the success step should process without an error message returned
    
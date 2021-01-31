@droolsRuleProcessing @manual
Feature: Drools rule processing

  Scenario: Drools runtime can retrieve sessions defined in session definition model
    Given sessions named SessionA and SessionB are defined in the model
    When I request the processing of SessionA and SessionB
    Then I get a successful message

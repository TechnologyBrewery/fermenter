@generationStats
Feature: Capture statistics about code generation
  As a project leveraging or considering Fermenter, I want to statistics about the code generation provided by Fermenter
    so I can understand the contribution that Fermenter is making to my project.

    Scenario: Capture file size of overwritable targets
        Given statistics are enabled
        And an overwritable target is selected
        When the generators are executed
        Then the total file size of the generated files is captured

    Scenario: Capture file size of non-overwritable, non-existent targets
        Given statistics are enabled
        And a non-overwritable target is selected
        And the target does not exist
        When the generators are executed
        Then the total file size of the generated files is captured

    Scenario: Capture file size of overwritable, preexisting targets
        Given statistics are enabled
        And a non-overwritable target is selected
        And the target already exists
        When the generators are executed
        Then the total file size of the generated files is captured

    Scenario: Capture file size of multiple targets
        Given statistics are enabled
        And multiple targets are selected
        When the generators are executed
        Then the total file size of the generated files is captured

    Scenario: File size is not captured when stats are disabled
        Given statistics are not enabled
        And multiple targets are selected
        When the generators are executed
        Then the file size is not recorded


@priorityMessage
Feature: Support replay of priority messages to the user after session end

    Scenario: A priority message without metadata is generated and replayed at the end
        Given a priority message without metadata
        When the priority message(s) is(are) added to the maven session
        Then a priority message without metadata should be displayed at session end

    Scenario: A priority message with metadata is generated and replayed at the end
        Given a priority message with metadata
        When the priority message(s) is(are) added to the maven session
        Then a priority message with metadata should be displayed at session end

    Scenario: Multiple priority messages are generated and replayed at the end
        Given multiple priority messages
        When the priority message(s) is(are) added to the maven session
        Then the priority messages should be displayed at session end

    Scenario: No priority messages are generated and replayed at the end
        Given no priority messages
        When the priority message(s) is(are) added to the maven session
        Then no priority messages should be displayed at session end

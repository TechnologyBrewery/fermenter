@createNotifications
Feature: Support the ability to create notifications that can be output at the end of the build

    Scenario Outline: Add notifications for files
        Given a configuration that triggers <notificationsPerFile> notification for manual action to be taken in <numberOfFiles>
        When the MDA plugin runs
        Then <notificationsPerFile> are registered for output for <numberOfFiles> files

        Examples:
            | numberOfFiles | notificationsPerFile |
            | 1             | 1                    |
            | 2             | 3                    |
            | 3             | 4                    |

    Scenario Outline: Notification information is found in the generated manual action message
        Given a configuration that triggers a notification for manual action with a "<key>", "<items>", and programmatic value
        When the MDA plugin runs
        Then the resulting message contains the "<key>", "<items>", and programmatic value

        Examples:
            | key                | items             |
            | yesterday          | mccartney         |
            | girl               | lennon            |
            | we-can-work-it-out | lennon, mccartney |

    @manual
    Scenario Outline: Hide manual actions to suppress notification output
        Given a configuration that triggers a notification for manual action with a "<key>", "<items>", and programmatic value
        And hide manual actions is enabled
        When the MDA plugin runs
        Then the resulting message does NOT contain the "<key>", "<items>", and programmatic value

        Examples:
            | key                               | items     |
            | you've-got-to-hide-your-love-away | lennon    |
            | you-won't-see-me                  | mccartney |

    @manual
    Scenario: Collect per-project written manual action notifications and emit them at the end of the build
        Given one or more Maven modules that create manual action notifications in their "target/manual-action-notifications" folder
        When the end of the Maven build is reached
        Then the notifications for the Maven modules that were part of the build are emitted to the console

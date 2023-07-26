@generationContext
Feature: Expose project data to generation context

    Scenario: The execution root is available to generators
        Given generation in a Maven submodule
        When the generation context is created
        Then access to the root module's base directory is available

    Scenario: The root artifact ID is available to generators
        Given generation in a Maven submodule
        When the generation context is created
        Then access to the root module's artifact ID is available

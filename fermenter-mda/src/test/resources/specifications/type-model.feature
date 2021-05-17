@type
Feature: Specify types to support model-driven file generation

  Scenario Outline: Create a valid type file
    Given a type described by "<name>", "<fullyQualifiedImplementation>", "<shortImplementation>"
    When types are read
    Then a valid type is available can be looked up name "<name>"

    Examples: 
      | name   | fullyQualifiedImplementation       | shortImplementation |
      | string | java.lang.String                   | String              |
      | myType | org.bitbucket.fermenter.CustomType | CustomType          |

  Scenario Outline: Type name is not provided
    Given a type described by "<name>", "<fullyQualifiedImplementation>", "<shortImplementation>"
    When types are read
    Then the generator throws an exception about invalid type metadata

    Examples: 
      | name | fullyQualifiedImplementation       | shortImplementation |
      |      | java.lang.String                   | String              |
      |      | org.bitbucket.fermenter.CustomType | CustomType          |

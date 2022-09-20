@languageSpecificPluginConfig
Feature: Automatically re-configure target generation folders based on the specified language of the project to generate

    Scenario Outline: Automatically set Java or Python specific generated project defaults
        Given a Maven project named "<projectName>" with Fermenter Maven plugin configuration
        When the project's pom.xml is processed
        And the Fermenter Maven plugin configuration is validated
        Then Fermenter Maven plugin uses "<mainSourceRoot>" as the main source root folder, "<generatedSourceRoot>" as the generated source root folder, and "<namespace>" as the base package

        Examples:
            | projectName              | mainSourceRoot               | generatedSourceRoot                    | namespace                |
            | python-habushu-packaging | src/python_habushu_packaging | src/python_habushu_packaging/generated | python_habushu_packaging |
            | python-language-config   | src/python_language_config   | src/python_language_config/generated   | python_language_config   |
            | java-default-config      | src/main                     | src/generated                          | org.bitbucket.fermenter  |

    Scenario: Missing base package for Java project is detected as invalid
        Given a Maven project named "java-no-base-package" with Fermenter Maven plugin configuration
        When the project's pom.xml is processed
        And the invalid Fermenter Maven plugin configuration is attempted to be validated
        Then a validation error is detected

@target
Feature: Specify targets to support model-driven file generation

  Scenario Outline: Create a valid target file
    Given a target described by "<name>", "<generator>", "<template>", "<destination>", "<overwritable>", "<artifactType>"
    When targets are read
    Then a valid target is available can be looked up name "<name>"

    Examples: 
      | name    | generator                   | template | destination | overwritable | artifactType |
      | targetA | o.b.f.s.m.g.EntityGenerator | foo.vm   | Bar.java    | true         | main         |
      | targetB | o.b.f.s.m.g.EntityGenerator | blah.vm  | blah.xml    | false        | test         |

  Scenario Outline: Target name is not provided
    Given a target described by "<name>", "<generator>", "<template>", "<destination>", "<overwritable>", "<artifactType>"
    When targets are read
    Then the generator throws an exception about invalid metadata

    Examples: 
      | name    | generator                   | template | destination   | overwritable | artifactType |
      |         | o.b.f.s.m.g.EntityGenerator | foo.vm   | Bar.java      | true         | main         |
      | targetB |                             | blah.vm  | blah.xml      | false        | main         |
      | targetC | o.b.f.s.m.g.EntityGenerator |          | something.xml | false        | main         |
      | targetD | o.b.f.s.m.g.EntityGenerator | doh.vm   |               | false        | main         |
    
  Scenario: If unspecified, artifactType defaults to main
  	Given a target described with without an artifact type value
    When targets are read
    Then a valid target is available and has an artifact type of "main"
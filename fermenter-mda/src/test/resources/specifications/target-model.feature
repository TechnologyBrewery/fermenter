Feature: Specify targets to support model-driven file generation

  Scenario Outline: Create a valid target file
    Given a target described by "<name>", "<generator>", "<template>", "<destination>", "<overwritable>"
    When targets are read
    Then a valid target is available can be looked up name "<name>"

    Examples: 
      | name    | generator                   | template | destination | overwritable |
      | targetA | o.b.f.s.m.g.EntityGenerator | foo.vm   | Bar.java    | true         |
      | targetB | o.b.f.s.m.g.EntityGenerator | blah.vm  | blah.xml    | false        |

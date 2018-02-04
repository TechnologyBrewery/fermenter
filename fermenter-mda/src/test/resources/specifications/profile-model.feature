Feature: Specify profiles to support model-driven file generation

  Background: 
    Given the following targets
      | name    | generator                   | templateName | outputFile | overwritable |
      | targetA | o.b.f.s.m.g.EntityGenerator | foo.vm       | Bar.java   | true         |
      | targetB | o.b.f.s.m.g.EntityGenerator | blah.vm      | blah.xml   | false        |
      | targetC | o.b.f.s.m.g.EntityGenerator | bar.vm       | bar.json   | false        |
      | targetD | o.b.f.s.m.g.EntityGenerator | doo.vm       | Dar.java   | true         |
      | targetE | o.b.f.s.m.g.EntityGenerator | elah.vm      | elah.xml   | false        |
      | targetF | o.b.f.s.m.g.EntityGenerator | far.vm       | far.json   | false        |

  Scenario Outline: load a profile with no targets
    Given a profile described by "<name>", "<targetReferences>", "<profileReferences>"
    When profiles are read
    Then a valid EMPTY profile is available as "<name>"

    Examples: 
      | name     | targetReferences | profileReferences |
      | profile1 |                  |                   |
      | profile2 |                  |                   |

  Scenario Outline: load a profile with directly referenced targets
    Given a profile described by "<name>", "<targetReferences>", "<profileReferences>"
    When profiles are read
    Then a valid profile is available as "<name>" that contains <expectedTargetCount> targets

    Examples: 
      | name     | targetReferences        | profileReferences | expectedTargetCount |
      | profile3 | targetA,targetB         |                   |                   2 |
      | profile4 | targetC                 |                   |                   1 |
      | profile4 | targetA,targetB,targetC |                   |                   3 |

  Scenario: load a profile that references another profile and also contains targets
    Given a profile described by "profileW", "targetA,targetB", ""
    And a profile described by "profileX", "targetC,targetD", "profileW"
    When profiles are read
    Then a valid profile is available as "profileX" that contains 4 targets

  Scenario: load a profile that  references another profile but does NOT contains targets
    Given a profile described by "profileY", "targetE,targetF", ""
    And a profile described by "profileZ", "", "profileY"
    When profiles are read
    Then a valid profile is available as "profileZ" that contains 2 targets

  Scenario: a profile references itself, gracefully skipping self-reference
    Given a profile described by "profileY", "targetE,targetF", "profileY"
    When profiles are read
    Then a valid profile is available as "profileY" that contains 2 targets

  Scenario Outline: a profile references non-existent targets, gracefully skipping them
    Given a profile described by "<name>", "<targetReferences>", "<profileReferences>"
    When profiles are read
    Then a valid profile is available as "<name>" that contains <expectedTargetCount> targets

    Examples: 
      | name              | targetReferences                    | profileReferences | expectedTargetCount |
      | profileLittleOff1 | targetDoesNotExist                  |                   |                   0 |
      | profileLittleOff2 | targetC,targetDoesNotExist          |                   |                   1 |
      | profileLittleOff3 | targetDoesNotExist,notReallyIAmFake |                   |                   0 |

  Scenario Outline: a profile references non-existent profiles, gracefully skipping them
    Given a profile described by "<name>", "<targetReferences>", "<profileReferences>"
    When profiles are read
    Then a valid profile is available as "<name>" that contains <expectedTargetCount> targets

    Examples: 
      | name              | targetReferences | profileReferences           | expectedTargetCount |
      | profileLittleOff4 |                  | notItProfile1               |                   0 |
      | profileLittleOff5 | targetC          | notItProfile2               |                   1 |
      | profileLittleOff6 |                  | notItProfile1,notItProfile2 |                   0 |
      | profileLittleOff5 | targetC,targetD  | notItProfile2               |                   2 |
      | profileLittleOff5 | targetC,targetD  | notItProfile1,notItProfile2 |                   2 |

  Scenario: Profile name is not provided
    Given a profile described by "", "targetC,targetD", "profileDoesNotExist"
    When profiles are read
    Then the generator throws an exception about invalid profile metadata

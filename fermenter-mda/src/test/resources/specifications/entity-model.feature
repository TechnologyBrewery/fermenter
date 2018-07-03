@entity
Feature: Specify entities for use in model-driven file generation

  Scenario Outline: specify named entities via a JSON metamodel
    Given an entity named "<name>" in "<package>" with documentation "<documentation>"
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with the documentation "<documentation>"

    Examples: 
      | name | package         | documentation        |
      | Foo  | test.entity     | My purpose           |
      | Bar  | test.entity.alt | My different purpose |

  Scenario Outline: specify an entity parent information via a JSON metamodel
    Given an entity named "<parent>" in "<package>"
    And an entity name "<name>" in "<package>" with parent "<parent>" and inheritance strategy "<inheritanceStrategy>"
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with parent "<parent>" and inheritance strategy "<inheritanceStrategy>"

    Examples: 
      | name | package            | parent | inheritanceStrategy |
      | Foo  | test.entity.parent | Alpha  | mapped-superclass   |
      | Bar  | test.entity.parent | Beta   | mapped-superclass   |

  Scenario Outline: specify an entity parent that defaults/aligns inheritance strategy via a JSON metamodel
    Given an entity named "<parent>" in "<package>"
    And an entity name "<name>" in "<package>" with parent "<parent>" and inheritance strategy "<inheritanceStrategy>"
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with parent "<parent>" and inheritance strategy "<expectedInheritanceStrategy>"

    Examples: 
      | name | package            | parent | inheritanceStrategy | expectedInheritanceStrategy |
      | Foo  | test.entity.parent | Alpha  | MAPPED-superCLASS   | mapped-superclass           |
      | Bar  | test.entity.parent | Beta   |                     | mapped-superclass           |

  Scenario Outline: specify an table name via a JSON metamodel
    Given an entity named "<name>" in "<package>" with table "<table>"
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with table "<table>"

    Examples: 
      | name | package         | table |
      | Foo  | test.entity     | FOO   |
      | Bar  | test.entity.alt | BAR   |

  Scenario Outline: specify a lock strategy via a JSON metamodel
    Given an entity named "<name>" in "<package>" with table "<lockStrategy>"
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with a lock strategy of "<lockStrategy>"

    Examples: 
      | name | package         | lockStrategy |
      | Foo  | test.entity     | optimistic   |
      | Bar  | test.entity.alt | optimistic   |

  Scenario Outline: default a lock strategy via a JSON metamodel
    Given an entity named "<name>" in "<package>" with table "<lockStrategy>"
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with a lock strategy of "<expectedLockStrategy>"

    Examples: 
      | name | package           | lockStrategy | expectedLockStrategy |
      | Foo  | test.lock.default |              | optimistic           |
      | Bar  | test.default.alt  |              | optimistic           |

  Scenario Outline: specify a transient entity with defaulting via a JSON metamodel
    Given an entity named "<name>" in "<package>" with transient flag "<transient>"
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with a transient flag of "<transient>"

    Examples: 
      | name | package         | transient |
      | Foo  | test.entity     | true      |
      | Bar  | test.entity.alt | false     |

  Scenario Outline: default a transient entity with defaulting via a JSON metamodel
    Given an entity named "<name>" in "<package>" with transient flag "<transient>"
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with a transient flag of "<expectedTransient>"

    Examples: 
      | name | package         | transient | expectedTransient |
      | Foo  | test.entity     |           | false             |
      | Bar  | test.entity.alt |           | false             |

  Scenario Outline: specify an identifier via a JSON metamodel
    Given an entity named "<name>" in "<package>" with an indentifier:
      | name        | documentation   | type   | column   |
      | <fieldName> | <documentation> | <type> | <column> |
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with the following identifier:
      | name        | documentation   | type   | column   |
      | <fieldName> | <documentation> | <type> | <column> |

    Examples: 
      | name | package         | fieldName | documentation      | type    | column |
      | Foo  | test.entity     | FooId     | Primary key of Foo | string  | FOO_ID |
      | Bar  | test.entity.alt | BarId     | Primary key of Bar | integer | BAR_ID |

  Scenario Outline: specify a field via a JSON metamodel
    Given an entity named "<name>" in "<package>" with a field:
      | name        | documentation   | type   | column   |
      | <fieldName> | <documentation> | <type> | <column> |
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with the following field:
      | name        | documentation   | type   | column   |
      | <fieldName> | <documentation> | <type> | <column> |

    Examples: 
      | name | package         | fieldName | documentation  | type    | column  |
      | Bar  | test.entity.alt | summary   | Summary of Bar | string  | SUMMARY |
      | Blah | test.entity.alt | stuff     | Stuff of Blah  | integer | STUFF   |

  Scenario Outline: specify a field via a JSON metamodel that has an enumeration type
    Given an entity named "<name>" in "<package>" with a field:
      | name        | documentation   | type   | fieldPackage   | column   | required   |
      | <fieldName> | <documentation> | <type> | <fieldPackage> | <column> | <required> |
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with the following field:
      | name        | documentation   | type   | fieldPackage   | column   | required |
      | <fieldName> | <documentation> | <type> | <fieldPackage> | <column> | required |

    Examples: 
      | name | package         | fieldName | documentation  | type     | fieldPackage | column  | required |
      | Foo  | test.entity     | name      | Name of Foo    | SomeEnum | test.enum    | NAME    | true     |
      | Bar  | test.entity.alt | summary   | Summary of Bar | Enum1    | test.package | SUMMARY | false    |
      | Blah | test.entity.alt | stuff     | Stuff of Blah  | Enum3    | test.foo.bar | STUFF   | true     |

  Scenario Outline: specify a reference with via a JSON metamodel
    Given an entity named "<type>" in "<referencePackage>" with an indentifier:
      | name | column |
      | id   | FOO_ID |
    And an entity named "<name>" in "<package>" with an reference:
      | referenceName   | documentation   | type   | referencePackage   | localColumn   | required   |
      | <referenceName> | <documentation> | <type> | <referencePackage> | <localColumn> | <required> |
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with the following reference:
      | referenceName   | documentation   | type   | referencePackage   | localColumn   | required   |
      | <referenceName> | <documentation> | <type> | <referencePackage> | <localColumn> | <required> |

    Examples: 
      | name | package         | referenceName  | documentation | type  | referencePackage | localColumn | required |
      | Foo  | test.entity     | AlphaReference | M-1 to Alpha  | Alpha | greek.stuff      | ALPHA_ID    | true     |
      | Bar  | test.entity.alt | BetaReference  | M-1 to Beta   | Beta  | greek.stuff      | BETA_ID     | false    |

  Scenario Outline: specify a relation with via a JSON metamodel
    Given an entity named "<type>" in "<relationPackage>" with an indentifier:
      | name | column |
      | id   | BAR_ID |
    And an entity named "<name>" in "<package>" with a relation:
      | documentation   | type   | relationPackage   | localColumn   | fetchMode   | multiplicity   |
      | <documentation> | <type> | <relationPackage> | <localColumn> | <fetchMode> | <multiplicity> |
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with the following relation:
      | documentation   | type   | relationPackage   | localColumn   | fetchMode   | multiplicity   |
      | <documentation> | <type> | <relationPackage> | <localColumn> | <fetchMode> | <multiplicity> |

    Examples: 
      | name | package         | documentation | type  | relationPackage | localColumn | fetchMode | multiplicity |
      | Foo  | test.entity     | 1-M to Delta  | Delta | greek.stuff     | DELTA_ID    | eager     | 1-M          |
      | Bar  | test.entity.alt | 1-M to Gamma  | Gamma | greek.stuff     | GAMMA_ID    | lazy      | 1-1          |
      | BB   | test.entity.alt | foo           | Gamma | greek.stuff     | GAMMA_ID    | lazy      | M-M          |

  Scenario Outline: Multiplicity is defaulted to 1-M when it is not present
    Given an entity named "<name>" in "<package>" with a valid relation that does not specify multiplicity
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with the following relation that has one-to-many multiplicity

    Examples: 
      | name | package         |
      | Foo  | test.entity     |
      | Bar  | test.entity.alt |

  Scenario Outline: Invalid multiplicity results in an error
    Given an entity named "<name>" in "<package>" with an invalid multiplicity "<invalidMultiplicity>"
    When entities are read
    Then the tracker reports that errors were encountered

    Examples: 
      | name | package         | invalidMultiplicity |
      | Foo  | test.entity     | lots                |
      | Bar  | test.entity.alt | a-bunch             |

  Scenario Outline: Fetch mode is defaulted to EAGER when it is not present
    Given an entity named "<name>" in "<package>" with a valid relation that does not specify fetch mode
    When entities are read
    Then an entity metamodel instance is returned for the name "<name>" in "<package>" with the following relation that has eager fetch mode

    Examples: 
      | name | package         |
      | Foo  | test.entity     |
      | Bar  | test.entity.alt |

  @fast
  Scenario Outline: Invalid fetch mode results in an error
    Given an entity named "<name>" in "<package>" with an invalid fetch mode "<invalidFetchMode>"
    When entities are read
    Then the tracker reports that errors were encountered

    Examples: 
      | name | package         | invalidFetchMode  |
      | Foo  | test.entity     | whenYouFeelLikeIt |
      | Bar  | test.entity.alt | eagerly           |

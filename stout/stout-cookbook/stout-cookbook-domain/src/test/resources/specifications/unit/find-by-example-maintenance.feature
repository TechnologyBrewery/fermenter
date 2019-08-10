@findByExample
Feature: Find By Example

    As a user, I want to find data using the find by example maintenance service


    Scenario Outline: query by example and check results
        Given the following simple domain objects:
            | name | type | theLong1 | anEnumeratedValue | standardBoolean |
            | Joe  | Dog  | 1234     | SECOND            | true            |
            | Joe  | Dog  | 2        | SECOND            | true            |
            | Joe  | Cat  | 2        | SECOND            | true            |
            | Joe  | Cat  | 3        | SECOND            | true            |
            | Joe  | Cat  | 3        | FIRST             | true            |
            | Bob  | Cat  | 3        | FIRST             | false           |
        And the probe has a name of "<name>" and type of "<type>" and long of "<theLong1>" and enum of "<anEnumeratedValue>" and boolean of "<standardBoolean>"
        When I find by the example
        Then I should get "<countOfResults>" results that match the probe inputs

        Examples:
            | name | type | theLong1 | anEnumeratedValue | standardBoolean | countOfResults |
            # search for very exactly one
            | Joe  | Dog  | 1234     | SECOND            | true            | 1              |
            # search for all Joes
            | Joe  |      |          |                   |                 | 5              |
            # search by enum
            |      |      |          | FIRST             |                 | 2              |
            # search for a string and enum together
            | Joe  |      |          | SECOND            |                 | 4              |
            # search for boolean and long together
            |      |      | 3        |                   | true            | 2              |



    Scenario Outline: Find by example supports searching by contains matcher
        Given the following simple domain objects:
            | name | type | theLong1 | anEnumeratedValue | standardBoolean |
            | Joe1 | Dog  | 1234     | SECOND            | true            |
            | Joe2 | Dog  | 2        | SECOND            | true            |
            | Joe3 | Cat  | 2        | SECOND            | true            |
            | Bob1 | Cat  | 3        | SECOND            | true            |
            | Bob2 | Cat  | 3        | FIRST             | true            |
            | Bob3 | Cat  | 3        | FIRST             | false           |
        And the probe has a name of "<name>" and type of "<type>" and long of "<theLong1>" and enum of "<anEnumeratedValue>" and boolean of "<standardBoolean>"
        When I find by example using contains matching
        Then I should get the following results back "<names>" where the attributes contains the search ignoring case

        Examples:
            | name | type | theLong1 | anEnumeratedValue | standardBoolean | names                        |
            | Joe  |      |          |                   |                 | Joe1, Joe2, Joe3             |
            # capitalization is ignored
            | JOE  |      |          |                   |                 | Joe1, Joe2, Joe3             |
            | oe   |      |          |                   |                 | Joe1, Joe2, Joe3             |
            |      | Cat  |          |                   |                 | Joe3, Bob1, Bob2, Bob3       |
            |      | at   |          |                   |                 | Joe3, Bob1, Bob2, Bob3       |
            # only returns exact numbers for numberical contains
            |      |      | 2        |                   |                 | Joe2, Joe3                   |
            |      |      |          | SECOND            |                 | Joe1, Joe2, Joe3, Bob1       |
            # only returns exact matches for booleans
            |      |      |          |                   | true            | Joe1, Joe2, Joe3, Bob1, Bob2 |
            |      |      |          |                   | false           | Bob3                         |

    Scenario: All results are returned when no examples are provided to the query
        Given simple domains exist in the system
        When I query for simple domains with no examples provided
        Then all simple domains are returned


    Scenario: query with a null probe (will default to find all)
        Given the following simple domain objects:
            | name |
            | Joe  |
            | Jack |
        And a null probe
        When I find by the example
        Then I should get "2" results because null probe defaults to all

    Scenario: query with sort on string
        Given the following simple domain objects:
            | name | theLong1 |
            | CCCC | 3        |
            | BBBB | 2        |
            | AAAA | 1        |
        And an empty probe
        And a sort by "name" column
        When I find by the example
        Then I should get all simple domains sorted by name:
            | name | theLong1 |
            | AAAA | 1        |
            | BBBB | 2        |
            | CCCC | 3        |


    Scenario: query with sort on long and string
        Given the following simple domain objects:
            | name | theLong1 |
            | BBBB | 1        |
            | BBBB | 2        |
            | AAAA | 3        |
        And an empty probe
        And a sort by "name" column
        And a sort by "theLong1" column
        When I find by the example
        Then I get all simple domains sorted by name then long:
            | name | theLong1 |
            | AAAA | 3        |
            | BBBB | 1        |
            | BBBB | 2        |

    Scenario: query with no sort
        When I find by the example with a null sort
        Then I should get an error message saying "When searching by example, a valid sort is required."



@jwtToken
Feature: Create JSON Web Tokens based on PDP rules

  Scenario Outline: create a token containing only default claims
    When a token is requested for "<subject>" and "<audience>"
    Then the token contains claims for "<subject>", "<audience>", and "<issuer>"
    And a claim with not before time skew of 60 seconds from the issue time
    And a claim with an expiration time 3600 seconds from the issue time with an addition 60 seconds for skew

    Examples: 
      | subject       | audience                                      | issuer      |
      | tonyGwynn     | petco park fans                               | unspecified |
      | trevorHoffman | fans that stuck it out to the end of the game | unspecified |

  Scenario: create a token with PDP rule claims
    When a token is requested for "tonyGwynn" and "unitTest"
    And the following claims:
      | name                         | resource   | action                            |
      | canSingle                    | hit/single |                                   |
      | canStrikeout                 |            | strikeout                         |
      | hitOver350In84               |            | hit-over-350-in-1984              |
      | hasReggieJacksonJerseyNumber |            | reggie-jackson-jersey-number-test |
    Then a claim is returned with the following rule and decision pairings:
      | name                         | resource   | action                            | result         |
      | canSingle                    | hit/single |                                   | PERMIT         |
      | canStrikeout                 |            | strikeout                         | DENY           |
      | hitOver350In84               |            | hit-over-350-in-1984              | PERMIT         |
      | hasReggieJacksonJerseyNumber |            | reggie-jackson-jersey-number-test | NOT_APPLICABLE |

  Scenario Outline: create token with Attribute Store values
    When a token is requested for "<player>" with an attribute value claim for seasons batting over .350
    Then a claim is returned with the attributes "<seasonsBattingOver350>"

    Examples: 
      | player        | seasonsBattingOver350                    |
      | tonyGwynn     | 1984, 1987, 1993, 1994, 1995, 1996, 1997 |
      | reggieJackson |                                          |
      | kirbyPuckett  |                                     1988 |

  Scenario: create a token using an existing private key
    When a private key exists on the server
    And a token is requested for "tonyGwynn" and "unitTest"
    Then a token will be returned using the private key

#@advancedProfile
#Feature: Support dynamic creation of profiles based on command line interaction
#
#    Background:
#        Given the following profiles and related targets:
#            | profileName | family | targetNames      |
#            | profileA    | foo    | target1, target2 |
#            | profileB    | foo    | target3, target4 |
#            | profileC    | bar    | target5, target6 |
#            | profileD    | blah   | target3, target7 |
#
#    Scenario Outline: Discover implementation options for a given profile family
#        When profiles are read
#        And implementations for "<family>" are requested
#        Then the following "<expectedImplementations>" are return
#
#        Examples:
#            | family | expectedImplementations |
#            | foo    | profileA, profileB      |
#            | bar    | profileC                |
#            | blah   | profileD                |

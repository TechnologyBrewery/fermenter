@pagingSupport
Feature: Paging Support
    As a user, I want to be able to call a service and have it return a paged response.

    Scenario Outline: Paging services return statistics about the number of elements in the page
        Given there are 10 simple domain BOs in the system
        When I request page "<pageIndex>" of "<size>" number of simple domains
        Then the paged response indicates "<pageIndex>" as the page number
        And the paged response indicates "<size>" as the number of simple domains requested
        And the paged response indicates "<numberOfElements>" as the number of simple domains in the page content

        Examples:
            | pageIndex | size | numberOfElements |
            | 0         | 1    | 1                |
            | 0         | 2    | 2                |
            | 1         | 2    | 2                |
            | 1         | 2    | 2                |
            | 0         | 100  | 10               |

    Scenario: Edge Case: If a request asks for more items than available, then the size matches the request, and the numberOfElements matches the number returned
        Given there are 10 simple domain BOs in the system
        When I request page "0" of "100" number of simple domains
        Then the paged response indicates "100" as the size which indicates the number of simple domains requested
        And the paged response indicates "10" as the numberOfElements which indicates the number of simple domains in the page content

    Scenario Outline: Paging services return statistics about the total total pages and total numer of elements
        Given there are 10 simple domain BOs in the system
        When I request page "<pageIndex>" of "<size>" number of simple domains
        Then the paged response indicates "<size>" as the number of simple domains requested
        And the paged response indicates "<totalPages>" as the total number of pages available
        And the paged response indicates "<totalElements>" as the total number of BOs available

        Examples:
            | pageIndex | size | totalPages | totalElements |
            | 0         | 1    | 10         | 10            |
            | 0         | 2    | 5          | 10            |
            | 1         | 10   | 1          | 10            |

    Scenario Outline: Paging services indicate if it's the first or last page in the response statistics
        Given there are 10 simple domain BOs in the system
        When I request page "<pageIndex>" of "<size>" number of simple domains
        Then the paged response indicates "<isFirst>" if it is the first page available
        And the paged response indicates "<isLast>" if it is the last page available

        Examples:
            | pageIndex | size | isFirst | isLast |
            | 0         | 5    | true    | false  |
            | 1         | 5    | false   | true   |
            | 0         | 10   | true    | true  |

    Scenario: Page Index Error: Paging services throw an error if the page index is missing in the request
        When I request a paged service without providing a page index
        Then I get an error message back indicating that the page index is required

    Scenario: Page Size Error: Paging services throw an error if the page size is missing in the request
        When I request a paged service without providing a page size
        Then I get an error message back indicating that the page size is required

    Scenario: Error Negative Index: If a request asks for a negative page index then the service throws an error
        When I request a paged service for a negative page index
        Then I get an error message back indicating that the page index must be a positive number

    Scenario: Error Negative Size: If a request asks for a negative page size then the service throws an error
        When I request a paged service for a negative page size
        Then I get an error message back indicating that the page size must be a positive number

    Scenario: Paged service responses contain a list of BOs in the response page content
        Given there are 10 simple domain BOs in the system
        When I request all BOs from a service that provides paged responses
        Then I get back a response that contains a page
        And within the page content the list of simple domain BOs

    Scenario: Find by example maintenance services return paged responses with full details mentioned above
        Given there are 10 simple domain BOs in the system
        When I request all BOs from the find by example maintenance service
        Then I get back a paged response with all the appropriate page statistics
        # See scenarios above for full list of page stats that should be provided.
        And within the page content the list of simple domain BOs

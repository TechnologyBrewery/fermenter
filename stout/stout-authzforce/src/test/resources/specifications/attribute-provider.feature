@attributes
Feature: Specify custom attributes

  Scenario Outline: specify attributes with shorthand types
    Given an attribute with stout type "<stoutType>"
    When the attribute is read
    Then the fully qualified type "<fullyQualifiedType>" is returned

    Examples: 
      | stoutType | fullyQualifiedType                       |
      | string    | http://www.w3.org/2001/XMLSchema#string  |
      | boolean   | http://www.w3.org/2001/XMLSchema#boolean |
      | anyUri    | http://www.w3.org/2001/XMLSchema#anyURI  |
      | uri       | http://www.w3.org/2001/XMLSchema#anyURI  |
      | date      | http://www.w3.org/2001/XMLSchema#date    |
      | int       | http://www.w3.org/2001/XMLSchema#integer |
      | integer   | http://www.w3.org/2001/XMLSchema#integer |
      | double    | http://www.w3.org/2001/XMLSchema#double  |

  Scenario Outline: specify fully qualified type attributes WITHOUT shorthand
    Given an attribute with stout type "<stoutType>"
    When the attribute is read
    Then the fully qualified type "<fullyQualifiedType>" is returned

    Examples: 
      | stoutType                                  | fullyQualifiedType                         |
      | http://www.w3.org/2001/XMLSchema#hexBinary | http://www.w3.org/2001/XMLSchema#hexBinary |
      | http://www.w3.org/2001/XMLSchema#string    | http://www.w3.org/2001/XMLSchema#string    |
      | http://www.w3.org/2001/XMLSchema#boolean   | http://www.w3.org/2001/XMLSchema#boolean   |
      | http://www.w3.org/2001/XMLSchema#anyURI    | http://www.w3.org/2001/XMLSchema#anyURI    |
      | http://www.w3.org/2001/XMLSchema#anyURI    | http://www.w3.org/2001/XMLSchema#anyURI    |
      | http://www.w3.org/2001/XMLSchema#date      | http://www.w3.org/2001/XMLSchema#date      |
      | http://www.w3.org/2001/XMLSchema#integer   | http://www.w3.org/2001/XMLSchema#integer   |
      | http://www.w3.org/2001/XMLSchema#integer   | http://www.w3.org/2001/XMLSchema#integer   |
      | http://www.w3.org/2001/XMLSchema#double    | http://www.w3.org/2001/XMLSchema#double    |

  Scenario Outline: specify attributes with shorthand categories
    Given an attribute with stout category "<stoutCategory>"
    When the attribute is read
    Then the fully qualified category "<fullyQualifiedCategory>" is returned

    Examples: 
      | stoutCategory | fullyQualifiedCategory                                       |
      | resource      | urn:oasis:names:tc:xacml:3.0:attribute-category:resource     |
      | action        | urn:oasis:names:tc:xacml:3.0:attribute-category:action       |
      | subject       | urn:oasis:names:tc:xacml:1.0:subject-category:access-subject |

  Scenario Outline: specify fully qualified categories WITHOUT shorthand
    Given an attribute with stout category "<stoutCategory>"
    When the attribute is read
    Then the fully qualified category "<fullyQualifiedCategory>" is returned

    Examples: 
      | stoutCategory                                                | fullyQualifiedCategory                                       |
      | urn:oasis:names:tc:xacml:3.0:attribute-category:environment  | urn:oasis:names:tc:xacml:3.0:attribute-category:environment  |
      | urn:oasis:names:tc:xacml:3.0:attribute-category:resource     | urn:oasis:names:tc:xacml:3.0:attribute-category:resource     |
      | urn:oasis:names:tc:xacml:3.0:attribute-category:action       | urn:oasis:names:tc:xacml:3.0:attribute-category:action       |
      | urn:oasis:names:tc:xacml:1.0:subject-category:access-subject | urn:oasis:names:tc:xacml:1.0:subject-category:access-subject |

@stoutContentRepository
Feature: Manage content in Stout

  Scenario Outline: Save and retrieve content to the repository
    Given content that should be saved in "<folderName>" as "<fileName>"
    When the content is persisted
    Then the content can be retrieved from "<folderName>" as "<fileName>" without change

    Examples: 
      | folderName  | fileName        |
      | someFolder  | someContent.txt |
      | otherFolder | fileName.log    |

  Scenario Outline: Save and delete content
    Given content that has been saved in "<folderName>" as "<fileName>"
    When the content is deleted
    Then the content can no longer be retrieved from "<folderName>" as "<fileName>"

    Examples: 
      | folderName       | fileName             |
      | shortLivedFolder | shortLiveContent.txt |
      | otherTempFolder  | tempFileName.log     |
